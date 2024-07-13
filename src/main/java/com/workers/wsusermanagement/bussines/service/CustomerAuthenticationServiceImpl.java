package com.workers.wsusermanagement.bussines.service;

import com.workers.wsusermanagement.bussines.interfaces.CustomerAuthenticationService;
import com.workers.wsusermanagement.bussines.service.signin.interfaces.SignInService;
import com.workers.wsusermanagement.bussines.service.signup.context.SignUpContext;
import com.workers.wsusermanagement.bussines.service.signup.interfaces.SignUpService;
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
        return signUpMapper.toCustomerServiceContext(request);
    }


    //2. Задача
    // редактирование профиля - здесь для реализации будет один сервис

    //3. Задача
    // получение данных профиля - просто список разрешенныых полей UserProfile

    //4. Задача
    // дополнительная активация - на случай, если с первого раза не удалось активировать пользователя.
    // Например не пришло смс.
    // То есть при попытке пользователя войти, будет висеть предупреждение, о том что не произошла активация профиля
    // и кнопка попробовать активировать еще раз
}
