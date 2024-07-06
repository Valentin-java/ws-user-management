package com.workers.wsusermanagement.bussines.service.signup.service;

import com.workers.wsusermanagement.bussines.service.signup.context.SignUpContext;
import com.workers.wsusermanagement.bussines.service.signup.interfaces.SignUpService;
import com.workers.wsusermanagement.persistence.enums.ActivityStatus;
import com.workers.wsusermanagement.persistence.mapper.CustomerMapper;
import com.workers.wsusermanagement.persistence.repository.UserProfileRepository;
import com.workers.wsusermanagement.rest.inbound.dto.SignUpResponse;
import com.workers.wsusermanagement.rest.outbound.feign.client.CustomerRegistryProcessFeignClient;
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

    private final CustomerRegistryProcessFeignClient customerRegistryProcessFeignClient;
    private final UserProfileRepository userProfileRepository;
    private final CustomerMapper customerMapper;

    @Override
    public SignUpResponse signUpProcess(SignUpContext ctx) {
        return Optional.of(ctx)
                .map(this::validateUniqueCustomer)
                .map(customerRegistryProcessFeignClient::requestToRegistryCustomer)
                .map(this::createCustomerProfile)
                .map(customerRegistryProcessFeignClient::requestToAssignRole)
                .map(customerRegistryProcessFeignClient::requestToActivationCustomer)
                .map(this::activationCustomerProfile) // далее активация в профиле
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Не удалось зарегистрировать клиента"));
    }

    private SignUpContext validateUniqueCustomer(SignUpContext request) {
        if (!userProfileRepository.existsUserProfileByUsername(request.getSignUpRequest().phoneNumber())) {
            return request;
        }
        throw new ResponseStatusException(BAD_REQUEST, "Пользователь в системе уже существует");
    }

    private SignUpContext createCustomerProfile(SignUpContext ctx) {
        var customerProfile = customerMapper.toEntity(ctx.getSignUpRequest());
        userProfileRepository.save(customerProfile);
        return ctx;
    }

    private SignUpResponse activationCustomerProfile(SignUpContext ctx) {
        var userProfile = userProfileRepository.findByUsername(ctx.getSignUpRequest().phoneNumber())
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, UNEXPECTED_ERROR_MESSAGE));
        userProfile.setActivityStatus(ActivityStatus.ACTIVE);
        userProfileRepository.save(userProfile);

        return new SignUpResponse(userProfile.getUsername(), "Пользователь успешно зарегистрирован");
    }
}
