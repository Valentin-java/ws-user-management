package com.workers.wsusermanagement.rest.outbound.feign.client.process.login;

import com.workers.wsusermanagement.bussines.service.signin.context.SignInContext;

public interface CustomerLoginProcessFeignClient {

    SignInContext requestToLoginCustomer(SignInContext ctx);
}
