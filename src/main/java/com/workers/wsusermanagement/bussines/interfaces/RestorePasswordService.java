package com.workers.wsusermanagement.bussines.interfaces;

import com.workers.wsusermanagement.bussines.service.signup.model.SignUpRequest;
import com.workers.wsusermanagement.bussines.service.signup.model.SignUpResponse;

public interface RestorePasswordService {

    SignUpResponse restorePasswordBeforeOtp(SignUpRequest request);

    SignUpResponse restorePasswordConfirmOtp(SignUpRequest request);

    SignUpResponse restorePasswordChangePassword(SignUpRequest request);
}
