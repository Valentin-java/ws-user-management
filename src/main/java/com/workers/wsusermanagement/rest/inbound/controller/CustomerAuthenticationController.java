package com.workers.wsusermanagement.rest.inbound.controller;

import com.workers.wsusermanagement.bussines.interfaces.CustomerAuthenticationService;
import com.workers.wsusermanagement.rest.inbound.dto.LoginUserDtoRequest;
import com.workers.wsusermanagement.rest.inbound.dto.OtpRequest;
import com.workers.wsusermanagement.rest.inbound.dto.PasswordRequest;
import com.workers.wsusermanagement.rest.inbound.dto.RegistryUserDtoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth/customer")
public class CustomerAuthenticationController {

    private final CustomerAuthenticationService customerAuthenticationService;

    // Первая фаза регистрации ввод имени и номера телефона
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody RegistryUserDtoRequest request) {
        return ResponseEntity.ok(customerAuthenticationService.signUp(request));
    }

    // Вторая фаза регистрации ввод и проверка отп. Регистрация исключительно только отп
    @PostMapping("/verify/sign-up")
    public ResponseEntity<?> verifySignUp(@RequestBody OtpRequest request) {
        return ResponseEntity.ok(customerAuthenticationService.verifySignUp(request));
    }

    // Первая фаза логина. Ввод номера телефона и идентификация способа логина далее отп/пароль
    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody LoginUserDtoRequest request) {
        return ResponseEntity.ok(customerAuthenticationService.signIn(request));
    }

    // Логин с помощью отп, в случае если НЕ задан пароль в профиле
    @PostMapping("/otp/sign-in")
    public ResponseEntity<?> signInByOtp(@RequestBody OtpRequest request) {
        return ResponseEntity.ok(customerAuthenticationService.signInByOtp(request));
    }

    // Логин с помощью пароля, в случае если задан пароль в профиле
    @PostMapping("/pass/sign-in")
    public ResponseEntity<?> signInByPassword(@RequestBody PasswordRequest request) {
        return ResponseEntity.ok(customerAuthenticationService.signInByPassword(request));
    }

    // Установить пароль из профиля пользователя
    @PostMapping("/pass/set-new-pass")
    public ResponseEntity<?> setNewPassword(@RequestBody OtpRequest request) {
        return ResponseEntity.ok(customerAuthenticationService.setNewPassword(request));
    }

    // Ручка для логина по паролю
    // ну и соотвественно на ЭФ ввода именно пароля будет ссылка на сброс пароля
}
