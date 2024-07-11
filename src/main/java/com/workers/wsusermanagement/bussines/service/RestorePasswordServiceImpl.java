package com.workers.wsusermanagement.bussines.service;

import com.workers.wsusermanagement.bussines.interfaces.RestorePasswordService;
import com.workers.wsusermanagement.bussines.service.reset.interfaces.ResetPasswordService;
import com.workers.wsusermanagement.bussines.service.reset.model.ResetPasswordResponse;
import com.workers.wsusermanagement.bussines.service.signup.model.SignUpRequest;
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
     * Здесь все проверим, деактивируем пользователя, сбросим пароль и отправим отп для восстановления.
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

    /**
     * Получим отп, проверим что именно такой отп именно этому пользователю отправляли, подтвердим, что все хорошо.
     * У отп поменять статус на активный
     *
     * @param request
     * @return
     */
    @Override
    public ResetPasswordResponse getConfirmationOtp(SignUpRequest request) {
        // Подтверждение отп -> смена статуса отп на активный
        return null;
    }

    /**
     * Здесь нужно проверить, что отп по которому ранее прошло потверждение имеет статус активный
     * @param request
     * @return
     */
    @Override
    public ResetPasswordResponse setNewPassword(SignUpRequest request) {
        // восстановление пароля
        // Получим отп
        // Проверим что пользователь существует
        // Проверим что данный отп принадлежит данному пользователю
        // Проверим статус отп
        // Если статус активный изменим пароль и установим в ws-auth
        // получим токен и отдадим
        return null;
    }
}
