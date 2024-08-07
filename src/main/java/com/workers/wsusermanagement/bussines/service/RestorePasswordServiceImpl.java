package com.workers.wsusermanagement.bussines.service;

import com.workers.wsusermanagement.bussines.interfaces.RestorePasswordService;
import com.workers.wsusermanagement.bussines.service.confirmotp.interfaces.ConfirmationOtpService;
import com.workers.wsusermanagement.bussines.service.resetpass.interfaces.ResetPasswordService;
import com.workers.wsusermanagement.bussines.service.resetpass.model.ResetPasswordResponse;
import com.workers.wsusermanagement.bussines.service.setpass.interfaces.ChangePasswordService;
import com.workers.wsusermanagement.bussines.service.signin.model.SignInResponse;
import com.workers.wsusermanagement.rest.inbound.dto.ChangePasswordRequest;
import com.workers.wsusermanagement.rest.inbound.dto.OtpRequest;
import com.workers.wsusermanagement.rest.inbound.dto.ResetPasswordInitRequest;
import com.workers.wsusermanagement.rest.inbound.mapper.ChangePasswordMapper;
import com.workers.wsusermanagement.rest.inbound.mapper.ConfirmationOtpMapper;
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
    private final ConfirmationOtpMapper confirmationOtpMapper;
    private final ConfirmationOtpService confirmationOtpService;
    private final ChangePasswordMapper changePasswordMapper;
    private final ChangePasswordService changePasswordService;

    /**
     * Здесь все проверим, сбросим пароль и отправим отп для восстановления.
     * @param request
     * @return
     */
    @Override
    public ResetPasswordResponse resetPasswordByOtp(ResetPasswordInitRequest request) {
        return Optional.of(request)
                .map(resetPasswordMapper::toServiceContext)
                .map(resetPasswordService::doProcess)
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
    public ResetPasswordResponse getConfirmationOtp(OtpRequest request) {
        return Optional.of(request)
                .map(confirmationOtpMapper::toServiceContext)
                .map(confirmationOtpService::doProcess)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, UNEXPECTED_ERROR_MESSAGE));
    }

    /**
     * Здесь нужно проверить, что отп по которому ранее прошло потверждение имеет статус активный
     * и сам пользователь НЕ активный
     * @param request
     * @return
     */
    @Override
    public SignInResponse setPasswordByOtp(ChangePasswordRequest request) {
        return Optional.of(request)
                .map(changePasswordMapper::toServiceContext)
                .map(changePasswordService::doProcess)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, UNEXPECTED_ERROR_MESSAGE));
    }
}
