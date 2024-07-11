package com.workers.wsusermanagement.bussines.service;

import com.workers.wsusermanagement.bussines.interfaces.CustomerAuthenticationService;
import com.workers.wsusermanagement.bussines.service.signin.interfaces.SignInService;
import com.workers.wsusermanagement.bussines.service.signup.context.SignUpContext;
import com.workers.wsusermanagement.bussines.service.signup.interfaces.SignUpService;
import com.workers.wsusermanagement.rest.inbound.dto.OtpRequest;
import com.workers.wsusermanagement.rest.inbound.dto.UserSignUpRequest;
import com.workers.wsusermanagement.rest.inbound.mapper.SignInMapper;
import com.workers.wsusermanagement.rest.inbound.mapper.SignUpMapper;
import org.springframework.stereotype.Service;

@Service
public class CustomerAuthenticationServiceImpl extends AbstractUserAuthenticationService implements CustomerAuthenticationService {

    private final SignUpMapper signUpMapper;

    public CustomerAuthenticationServiceImpl(SignUpService signUpService,
                                             SignInService signInService,
                                             SignUpMapper signUpMapper,
                                             SignInMapper signInMapper) {
        super(signUpService, signInService, signInMapper);
        this.signUpMapper = signUpMapper;
    }

    @Override
    protected SignUpContext mapToSignUpContext(UserSignUpRequest request) {
        return signUpMapper.toRegistryCustomerContext(request);
    }

    public void validateOtp(OtpRequest request) {
        // Потом надо будет ворваться в процесс signUp с валиадцией по отп

        // по номеру телефона ищем пользователя - номер отдается после отправки сообщения signingUp
        // берем его отп и сравниваем
        // получаем токен - отдаем кленту
    }


    //1. Задача
    // логин - восстановление профиля - 3х фазный. Требует доработки ендпоинтов в ws-auth.
    // и плюс три ендпоинта здесь
    //1. создать контроллер под восстоновлениетпороля
    //2. создать 3 метода контроллера
    // 3. ё,

    //2. Задача
    // редактирование профиля - здесь для реализации будет один сервис
    // что-то вроде EditProfileService. Будет на каждое обновление будет свой ендпоинт.

    //3. Задача
    // получение данных профиля - просто список разрешенныых полей UserProfile

    //4. Задача
    // дополнительная активация - на случай, если с первого раза не удалось активировать пользователя.
    // Например не пришло смс.
    // То есть при попытке пользователя войти, будет висеть предупреждение, о том что не произошла активация профиля
    // и кнопка попробовать активировать еще раз
}
