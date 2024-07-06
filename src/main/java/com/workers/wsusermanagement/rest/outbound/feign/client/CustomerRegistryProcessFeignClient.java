package com.workers.wsusermanagement.rest.outbound.feign.client;

import com.workers.wsusermanagement.bussines.service.signup.context.SignUpContext;

public interface CustomerRegistryProcessFeignClient {

    SignUpContext requestToRegistryCustomer(SignUpContext ctx);

    SignUpContext requestToAssignRole(SignUpContext ctx);

    SignUpContext requestToActivationCustomer(SignUpContext ctx);
}
