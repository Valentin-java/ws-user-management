package com.workers.wsusermanagement.bussines.service;

import com.workers.wsusermanagement.bussines.interfaces.RestorePasswordService;
import com.workers.wsusermanagement.bussines.service.signup.model.SignUpRequest;
import com.workers.wsusermanagement.bussines.service.signup.model.SignUpResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestorePasswordServiceImpl implements RestorePasswordService {

    @Override
    public SignUpResponse restorePasswordBeforeOtp(SignUpRequest request) {
        // восстановление пароля
        // Здесь все проверим и отправим отп

        // может быть сформировать временный технический токен ?
        // с уникальными id на период смены пароля
        return null;
    }

    @Override
    public SignUpResponse restorePasswordConfirmOtp(SignUpRequest request) {
        // восстановление пароля
        // Здесь проверим введенный отп, и сбросим пароль в ws-auth
        return null;
    }

    @Override
    public SignUpResponse restorePasswordChangePassword(SignUpRequest request) {
        // восстановление пароля
        // Здесь проверим введенный новый пароль и установим в ws-auth
        // получим токен и отдадим
        return null;
    }
}
