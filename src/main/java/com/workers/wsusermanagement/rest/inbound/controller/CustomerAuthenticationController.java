package com.workers.wsusermanagement.rest.inbound.controller;

import com.workers.wsusermanagement.bussines.interfaces.CustomerAuthenticationService;
import com.workers.wsusermanagement.rest.inbound.dto.OtpRequest;
import com.workers.wsusermanagement.rest.inbound.dto.UserSignInRequest;
import com.workers.wsusermanagement.rest.inbound.dto.UserSignUpRequest;
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

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody UserSignUpRequest request) {
        return ResponseEntity.ok(customerAuthenticationService.signUp(request));
    }

    @PostMapping("/verify/sign-up")
    public ResponseEntity<?> verifySignUp(@RequestBody OtpRequest request) {
        return ResponseEntity.ok(customerAuthenticationService.verifySignUp(request));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody UserSignInRequest request) {
        return ResponseEntity.ok(customerAuthenticationService.signIn(request));
    }

    @PostMapping("/otp/sign-in")
    public ResponseEntity<?> signInByOtp(@RequestBody OtpRequest request) {
        return ResponseEntity.ok(customerAuthenticationService.signInByOtp(request));
    }

    @PostMapping("/pass/sign-in")
    public ResponseEntity<?> signInByPassword(@RequestBody OtpRequest request) {
        return ResponseEntity.ok(customerAuthenticationService.signInByPassword(request));
    }

    // Надо возможность задать пароль для существующего пользователя

    // В ответе добавить по мимо uuid bool otp

    // Ручка для логина по паролю
    // ну и соотвественно на ЭФ ввода именно пароля будет ссылка на сброс пароля
}
