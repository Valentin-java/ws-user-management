package com.workers.wsusermanagement.bussines.service;

import com.workers.wsusermanagement.bussines.interfaces.CustomerService;
import com.workers.wsusermanagement.bussines.service.validation.signup.SignUpValidationService;
import com.workers.wsusermanagement.rest.inbound.dto.OtpRequest;
import com.workers.wsusermanagement.rest.inbound.dto.SignUpRequest;
import com.workers.wsusermanagement.rest.inbound.dto.SignUpResponse;
import com.workers.wsusermanagement.rest.outbound.feign.client.WsAuthFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final WsAuthFeignClient wsAuthFeignClient;

    @Override
    public SignUpResponse signingUp(SignUpRequest request) {
        Optional.of(request)
                .map(wsAuthFeignClient::requestToRegistryCustomer)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Не удалось зарегистрировать клиента"));

        return new SignUpResponse("+7911", "test ok");
//        return Optional.of(request)
//                .map(validationService::validate)
                // проверим нет ли у нас уже такого пользователя (здесь в будущем м.б. проверка блокировок и тп)
                // если все ок, отправляем смс/пуш
                // записываем/обновляем запись в БД вместе с хэш ПИН кодом(его тоже обновляем, при необходимости)
                // если запись новая - ставим роль, т.к. источник как Customer
                // отдаем 200 и номер телефона, если все ок, пользователя перекидывает на след ЭФ)
//                .map(e -> new SignUpResponse(""))
//                .orElse(null);
    }

    @Override
    public void validateOtp(OtpRequest request) {
        // по номеру телефона ищем пользователя - номер отдается после отправки сообщения signingUp
        // берем его отп и сравниваем
        // если все ок - идем в ws-auth регистрируем пользователя
        // получаем токен - отдаем кленту
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


}
