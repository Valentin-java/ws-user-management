package com.workers.wsusermanagement.rest.outbound.process.activation.client;

import com.workers.wsusermanagement.bussines.service.signup.context.SignUpContext;
import com.workers.wsusermanagement.rest.outbound.feign.WsAuthFeign;
import com.workers.wsusermanagement.rest.outbound.mapper.AuthRequestMapper;
import com.workers.wsusermanagement.rest.outbound.process.AbstractProcessFeignClient;
import com.workers.wsusermanagement.rest.outbound.process.activation.interfaces.UserActivationProcessFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserActivationProcessFeignClientImpl
        extends AbstractProcessFeignClient<SignUpContext>
        implements UserActivationProcessFeignClient {

    private final WsAuthFeign wsAuthFeign;
    private final AuthRequestMapper authRequestMapper;

    @Override
    protected SignUpContext mappingToRequest(SignUpContext ctx) {
        if (ctx.getAuthRequest() != null) {
            return ctx;
        }
        var authRequest = authRequestMapper.toAuthRequest(ctx.getRequest());
        ctx.setAuthRequest(authRequest);
        return ctx;
    }

    @Override
    protected SignUpContext doRequest(SignUpContext ctx) {
        var response = wsAuthFeign.registerCustomer(ctx.getAuthRequest());
        if (Boolean.TRUE.equals(response.getBody())) {
            return ctx;
        }
        throw new ResponseStatusException(BAD_REQUEST, "Не удалось активировать пользователя");
    }
}
