package com.workers.wsusermanagement.bussines.interfaces;

import com.workers.wsusermanagement.bussines.service.resetpass.model.ResetPasswordResponse;
import com.workers.wsusermanagement.bussines.service.signin.model.SignInResponse;
import com.workers.wsusermanagement.rest.inbound.dto.ChangePasswordRequest;
import com.workers.wsusermanagement.rest.inbound.dto.OtpRequest;
import com.workers.wsusermanagement.rest.inbound.dto.ResetPasswordInitRequest;

public interface RestorePasswordService {

    ResetPasswordResponse resetPasswordByOtp(ResetPasswordInitRequest request);

    ResetPasswordResponse getConfirmationOtp(OtpRequest request);

    SignInResponse setPasswordByOtp(ChangePasswordRequest request);
}
