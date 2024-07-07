package com.workers.wsusermanagement.bussines.service;

import com.workers.wsusermanagement.bussines.interfaces.CustomerService;
import com.workers.wsusermanagement.bussines.service.signin.interfaces.SignInService;
import com.workers.wsusermanagement.bussines.service.signin.model.SignInResponse;
import com.workers.wsusermanagement.bussines.service.signup.interfaces.SignUpService;
import com.workers.wsusermanagement.rest.inbound.dto.CustomerSignInRequest;
import com.workers.wsusermanagement.rest.inbound.dto.OtpRequest;
import com.workers.wsusermanagement.rest.inbound.dto.CustomerSignUpRequest;
import com.workers.wsusermanagement.bussines.service.signup.model.SignUpRequest;
import com.workers.wsusermanagement.bussines.service.signup.model.SignUpResponse;
import com.workers.wsusermanagement.rest.inbound.mapper.SignInMapper;
import com.workers.wsusermanagement.rest.inbound.mapper.SignUpMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static com.workers.wsusermanagement.util.CommonConstant.UNEXPECTED_ERROR_MESSAGE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final SignUpService signUpService;
    private final SignInService signInService;
    private final SignUpMapper signUpMapper;
    private final SignInMapper signInMapper;

    @Override
    public SignUpResponse signUp(CustomerSignUpRequest request) {
        return Optional.of(request)
                .map(signUpMapper::toRegistryCustomerContext)
                .map(signUpService::signUpProcess)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, UNEXPECTED_ERROR_MESSAGE));
    }

    @Override
    public SignInResponse signIn(CustomerSignInRequest request) {
        return Optional.of(request)
                .map(signInMapper::toLoginCustomerContext)
                .map(signInService::signInProcess)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, UNEXPECTED_ERROR_MESSAGE));
    }

    @Override
    public SignUpResponse restoreProfile(SignUpRequest request) {
        // восстановление аккаунта, если пароль забыли
        return null;
    }

    @Override
    public void validateOtp(OtpRequest request) {
        // Потом надо будет ворваться в процесс signUp с валиадцией по отп

        // по номеру телефона ищем пользователя - номер отдается после отправки сообщения signingUp
        // берем его отп и сравниваем
        // если все ок - идем в ws-auth регистрируем пользователя
        // получаем токен - отдаем кленту
    }


    // логин - стандартный
    // логин - восстановление профиля
    // редактирование профиля
    // получение данных профиля
    // дополнительная активация - на случай, если с первого раза не удалось активировать пользователя
}
