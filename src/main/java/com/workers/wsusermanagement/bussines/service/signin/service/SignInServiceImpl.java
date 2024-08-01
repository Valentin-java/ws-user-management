package com.workers.wsusermanagement.bussines.service.signin.service;

import com.workers.wsusermanagement.bussines.service.signin.context.SignInContext;
import com.workers.wsusermanagement.bussines.service.signin.interfaces.SignInService;
import com.workers.wsusermanagement.bussines.service.signin.model.LoginUserResponse;
import com.workers.wsusermanagement.persistence.enums.StatusOtp;
import com.workers.wsusermanagement.persistence.mapper.OtpEntityMapper;
import com.workers.wsusermanagement.persistence.repository.OtpEntityRepository;
import com.workers.wsusermanagement.persistence.repository.UserProfileRepository;
import com.workers.wsusermanagement.rest.outbound.process.notification.interfaces.SendSignInOtpNotificationClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.workers.wsusermanagement.util.CommonConstant.UNEXPECTED_ERROR_MESSAGE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignInServiceImpl implements SignInService {

    private final SendSignInOtpNotificationClient sendSignInOtpNotificationClient;
    private final UserProfileRepository userProfileRepository;
    private final OtpEntityRepository otpEntityRepository;
    private final OtpEntityMapper otpEntityMapper;

    @Override
    public LoginUserResponse doProcess(SignInContext ctx) {
        return Optional.of(ctx)
                .map(this::validateUniqueCustomer)
                .map(this::deactivateOtherOtp)
                .map(this::createOtpEntity)

                .map(sendSignInOtpNotificationClient::requestToExecuteByService)

                .map(this::updateVisitDate)
                .map(this::createResponse)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, UNEXPECTED_ERROR_MESSAGE));
    }

    private SignInContext validateUniqueCustomer(SignInContext ctx) {
        var userProfile = userProfileRepository.findByUsername(ctx.getRequest().phoneNumber())
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Пользователя не существует"));
        ctx.setUserProfile(userProfile);
        return ctx;
    }

    private SignInContext deactivateOtherOtp(SignInContext ctx) {
        if (!ctx.getUserProfile().getOtp()) return ctx;
        var otpEntityList = otpEntityRepository.findAllByUsername(ctx.getRequest().phoneNumber());
        otpEntityList.forEach(otp -> otp.setStatusOtp(StatusOtp.DECLINED));

        otpEntityRepository.saveAll(otpEntityList);
        return ctx;
    }

    private SignInContext createOtpEntity(SignInContext ctx) {
        if (!ctx.getUserProfile().getOtp()) return ctx;
        var otpEntity = otpEntityMapper.toSignUpOtpEntity(ctx);
        ctx.setOtpEntity(otpEntity);
        otpEntityRepository.save(otpEntity);
        return ctx;
    }

    private SignInContext updateVisitDate(SignInContext ctx) {
        if (!ctx.getUserProfile().getOtp()) return ctx;
        var userProfile = ctx.getUserProfile();
        userProfile.setLastVisitAt(LocalDateTime.now());
        userProfileRepository.save(userProfile);
        return ctx;
    }

    private LoginUserResponse createResponse(SignInContext ctx) {
        String uuid = ctx.getOtpEntity().getUuid() != null ? ctx.getOtpEntity().getUuid() : null;
        return new LoginUserResponse(uuid, ctx.getUserProfile().getOtp());
    }
}
