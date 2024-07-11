package com.workers.wsusermanagement.rest.outbound.process.login.interfaces;

import com.workers.wsusermanagement.bussines.service.signin.context.SignInContext;

public interface CustomerLoginProcessFeignClient {

    SignInContext requestToLoginCustomer(SignInContext ctx);
}
