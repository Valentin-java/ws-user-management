package com.workers.wsusermanagement.rest.outbound.process.registry.client;

import com.workers.wsusermanagement.bussines.service.signup.context.SignUpContext;
import com.workers.wsusermanagement.rest.outbound.feign.WsAuthFeign;
import com.workers.wsusermanagement.rest.outbound.mapper.AuthRequestMapper;
import com.workers.wsusermanagement.rest.outbound.process.AbstractProcessFeignClient;
import com.workers.wsusermanagement.rest.outbound.process.registry.interfaces.UserRegistryProcessFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRegistryProcessFeignClientImpl
        extends AbstractProcessFeignClient<SignUpContext>
        implements UserRegistryProcessFeignClient {

    private final WsAuthFeign wsAuthFeign;
    private final AuthRequestMapper authRequestMapper;

    @Override
    protected SignUpContext mappingToRequest(SignUpContext ctx) {
        var authRequest = authRequestMapper.toAuthRequest(ctx.getSignUpRequest());
        ctx.setAuthRequest(authRequest);
        return ctx;
    }

    @Override
    protected SignUpContext doRequest(SignUpContext ctx) {
        var response = wsAuthFeign.registerCustomer(ctx.getAuthRequest());
        if (Boolean.TRUE.equals(response.getBody())) {
            return ctx;
        }
        throw new ResponseStatusException(BAD_REQUEST, "Не удалось зарегистрировать пользователя");
    }
}
