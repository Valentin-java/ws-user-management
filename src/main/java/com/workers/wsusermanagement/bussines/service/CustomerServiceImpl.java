package com.workers.wsusermanagement.bussines.service;

import com.workers.wsusermanagement.bussines.interfaces.CustomerService;
import com.workers.wsusermanagement.bussines.service.validation.signup.SignUpValidationService;
import com.workers.wsusermanagement.rest.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final SignUpValidationService validationService;

    public void signingUp(SignUpRequest request) {
        // Валидация данных
        Optional.of(request)
                .map(validationService::validate)
                // если все ок, отправляем смс/пуш
                // делаем запись в свою БД вместе с ПИН кодом шифрованным
                // (ставим роль, т.к. источник как Customer)
                // отдаем 200 если все ок(там пользователя перекидывает на след ЭФ)
                .orElse(null);
    }



    //Регистрация. Как процсс вижу я:
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


}
