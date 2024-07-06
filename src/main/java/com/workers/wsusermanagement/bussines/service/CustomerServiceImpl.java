package com.workers.wsusermanagement.bussines.service;

import com.workers.wsusermanagement.bussines.interfaces.CustomerService;
import com.workers.wsusermanagement.bussines.service.signup.context.SignUpContext;
import com.workers.wsusermanagement.bussines.service.signup.interfaces.SignUpService;
import com.workers.wsusermanagement.persistence.enums.ActivityStatus;
import com.workers.wsusermanagement.persistence.enums.CustomerRole;
import com.workers.wsusermanagement.rest.inbound.dto.OtpRequest;
import com.workers.wsusermanagement.rest.inbound.dto.SignUpRequest;
import com.workers.wsusermanagement.rest.inbound.dto.SignUpResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final SignUpService signUpService;

    @Override
    public SignUpResponse signUp(SignUpRequest request) {
        var newCustomer = createProcessContext(request);
        return signUpService.signUpProcess(newCustomer);
    }

    @Override
    public SignUpResponse signIn(SignUpRequest request) {
        return null;
    }

    @Override
    public SignUpResponse restoreProfile(SignUpRequest request) {
        return null;
    }

    @Override
    public void validateOtp(OtpRequest request) {
        // по номеру телефона ищем пользователя - номер отдается после отправки сообщения signingUp
        // берем его отп и сравниваем
        // если все ок - идем в ws-auth регистрируем пользователя
        // получаем токен - отдаем кленту
    }

    private static SignUpContext createProcessContext(SignUpRequest request) {
        var newCustomer = new SignUpRequest(
                request.phoneNumber(),
                request.password(),
                ActivityStatus.INACTIVE,
                CustomerRole.CUSTOMER);
        return new SignUpContext().setSignUpRequest(newCustomer);
    }

    // в остальных вызовах мы будем узнавать пользователя по номеру телефона
    // номер телефона будет клиется в gateway во время валидации токена


    //Регистрация. Как процесс вижу я:
    //
    //В веб форме на мобильном устройстве пользователь вводит номер телефона.
    //
    //Мы валидируем номер телефона.
    //
    //Отправляем смс/пуш уведомление на моб. устройство для подтверждения аккаунта.
    //
    //Тем временем у пользователя переключается форма на ввод пин-кода.
    //
    //После ввода пин кода, мы проверяем достоверность пин кода, если да, то реегистрируем пользователя.
    //
    //Переключаем на главную форму, где можно будет уже разместить свой заказ.
    //
    //Так же будет висеть нотификация для пользователя, что необходимо дозаполнить свой профиль.


    // логин - стандартный
    // логин - восстановление профиля
}
