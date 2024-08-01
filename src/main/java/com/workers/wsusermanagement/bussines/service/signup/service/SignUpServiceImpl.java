package com.workers.wsusermanagement.bussines.service.signup.service;

import com.workers.wsusermanagement.bussines.service.signup.context.SignUpContext;
import com.workers.wsusermanagement.bussines.service.signup.interfaces.SignUpService;
import com.workers.wsusermanagement.bussines.service.signup.model.RegistryUserResponse;
import com.workers.wsusermanagement.persistence.enums.ActivityStatus;
import com.workers.wsusermanagement.persistence.enums.StatusOtp;
import com.workers.wsusermanagement.persistence.mapper.CustomerMapper;
import com.workers.wsusermanagement.persistence.mapper.OtpEntityMapper;
import com.workers.wsusermanagement.persistence.repository.OtpEntityRepository;
import com.workers.wsusermanagement.persistence.repository.UserProfileRepository;
import com.workers.wsusermanagement.rest.outbound.process.notification.interfaces.SendSignUpOtpNotificationClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static com.workers.wsusermanagement.util.CommonConstant.UNEXPECTED_ERROR_MESSAGE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {

    private final SendSignUpOtpNotificationClient senOtpNotificationFeignClient;
    private final UserProfileRepository userProfileRepository;
    private final OtpEntityRepository otpEntityRepository;
    private final OtpEntityMapper otpEntityMapper;
    private final CustomerMapper customerMapper;

    @Override
    public RegistryUserResponse doProcess(SignUpContext ctx) {
        return Optional.of(ctx)
                .map(this::validateUniqueCustomer)
                .map(this::validateUserStatus)
                .map(this::createCustomerProfile)
                .map(this::deactivateOtherOtp)
                .map(this::createOtpEntity)
                .map(senOtpNotificationFeignClient::requestToExecuteByService)
                .map(this::createResponse)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, UNEXPECTED_ERROR_MESSAGE));
    }

    private SignUpContext validateUniqueCustomer(SignUpContext request) {
        if (!userProfileRepository.existsUserProfileByUsername(request.getRequest().phoneNumber())) {
            return request;
        }
        throw new ResponseStatusException(BAD_REQUEST, "Пользователь уже существует");
    }

    private SignUpContext validateUserStatus(SignUpContext request) {
        if (!userProfileRepository.existsUserProfileByUsernameAndActivityStatus(
                request.getRequest().phoneNumber(), ActivityStatus.ACTIVE)) {
            return request;
        }
        throw new ResponseStatusException(BAD_REQUEST, "Пользователь уже активирован");
    }

    private SignUpContext createCustomerProfile(SignUpContext ctx) {
        var customerProfile = customerMapper.toEntity(ctx.getRequest());
        userProfileRepository.save(customerProfile);
        return ctx;
    }

    private SignUpContext deactivateOtherOtp(SignUpContext ctx) {
        var otpEntityList = otpEntityRepository.findAllByUsername(ctx.getRequest().phoneNumber());
        otpEntityList.forEach(otp -> otp.setStatusOtp(StatusOtp.DECLINED));

        otpEntityRepository.saveAll(otpEntityList);
        return ctx;
    }

    private SignUpContext createOtpEntity(SignUpContext ctx) {
        var otpEntity = otpEntityMapper.toSignUpOtpEntity(ctx);
        ctx.setOtpEntity(otpEntity);
        otpEntityRepository.save(otpEntity);
        return ctx;
    }

    private RegistryUserResponse createResponse(SignUpContext ctx) {
        return new RegistryUserResponse(ctx.getOtpEntity().getUuid());
    }
}
