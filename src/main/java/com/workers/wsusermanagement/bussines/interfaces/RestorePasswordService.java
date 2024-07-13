package com.workers.wsusermanagement.bussines.interfaces;

import com.workers.wsusermanagement.bussines.service.resetpass.model.ResetPasswordResponse;
import com.workers.wsusermanagement.bussines.service.signin.model.SignInResponse;
import com.workers.wsusermanagement.rest.inbound.dto.OtpRequest;
import com.workers.wsusermanagement.rest.inbound.dto.ResetUserPasswordRequest;
import com.workers.wsusermanagement.rest.inbound.dto.UserSignUpRequest;

public interface RestorePasswordService {

    ResetPasswordResponse resetPasswordByOtp(ResetUserPasswordRequest request);

    ResetPasswordResponse getConfirmationOtp(OtpRequest request);

    SignInResponse setPasswordByOtp(UserSignUpRequest request);
}
