package com.workers.wsusermanagement.bussines.service;

import com.workers.wsusermanagement.bussines.interfaces.RestorePasswordService;
import com.workers.wsusermanagement.bussines.service.reset.interfaces.ResetPasswordService;
import com.workers.wsusermanagement.bussines.service.reset.model.ResetPasswordResponse;
import com.workers.wsusermanagement.bussines.service.signup.model.SignUpRequest;
import com.workers.wsusermanagement.bussines.service.signup.model.SignUpResponse;
import com.workers.wsusermanagement.rest.inbound.dto.ResetUserPasswordRequest;
import com.workers.wsusermanagement.rest.inbound.mapper.ResetPasswordMapper;
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
public class RestorePasswordServiceImpl implements RestorePasswordService {

    private final ResetPasswordMapper resetPasswordMapper;
    private final ResetPasswordService resetPasswordService;

    /**
     * Здесь все проверим и отправим отп
     * @param request
     * @return
     */
    @Override
    public ResetPasswordResponse resetPasswordByOtp(ResetUserPasswordRequest request) {
        return Optional.of(request)
                .map(resetPasswordMapper::toLoginUserContext)
                .map(resetPasswordService::resetPasswordProcess)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, UNEXPECTED_ERROR_MESSAGE));
    }

    @Override
    public ResetPasswordResponse getConfirmationOtp(SignUpRequest request) {
        // восстановление пароля
        // Здесь проверим введенный отп, и сбросим пароль в ws-auth
        return null;
    }

    @Override
    public ResetPasswordResponse setNewPassword(SignUpRequest request) {
        // восстановление пароля
        // Здесь проверим введенный новый пароль и установим в ws-auth
        // получим токен и отдадим
        return null;
    }
}
