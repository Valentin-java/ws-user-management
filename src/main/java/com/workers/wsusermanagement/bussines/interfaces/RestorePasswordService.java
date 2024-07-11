package com.workers.wsusermanagement.bussines.interfaces;

import com.workers.wsusermanagement.bussines.service.reset.model.ResetPasswordResponse;
import com.workers.wsusermanagement.bussines.service.signup.model.SignUpRequest;
import com.workers.wsusermanagement.rest.inbound.dto.OtpRequest;
import com.workers.wsusermanagement.rest.inbound.dto.ResetUserPasswordRequest;

public interface RestorePasswordService {

    ResetPasswordResponse resetPasswordByOtp(ResetUserPasswordRequest request);

    ResetPasswordResponse getConfirmationOtp(OtpRequest request);

    ResetPasswordResponse setNewPassword(SignUpRequest request);
}
