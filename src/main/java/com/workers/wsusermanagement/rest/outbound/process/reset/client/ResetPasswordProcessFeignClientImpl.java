package com.workers.wsusermanagement.rest.outbound.process.reset.client;

import com.workers.wsusermanagement.bussines.service.reset.context.ResetPasswordContext;
import com.workers.wsusermanagement.rest.outbound.feign.WsAuthFeign;
import com.workers.wsusermanagement.rest.outbound.mapper.AuthRequestMapper;
import com.workers.wsusermanagement.rest.outbound.process.AbstractProcessFeignClient;
import com.workers.wsusermanagement.rest.outbound.process.reset.interfaces.ResetPasswordProcessFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResetPasswordProcessFeignClientImpl
        extends AbstractProcessFeignClient<ResetPasswordContext>
        implements ResetPasswordProcessFeignClient {

    private final WsAuthFeign wsAuthFeign;
    private final AuthRequestMapper authRequestMapper;

    @Override
    protected ResetPasswordContext mappingToRequest(ResetPasswordContext ctx) {
        var authRequest = authRequestMapper.toAuthRequest(ctx.getResetPasswordRequest());
        ctx.setAuthRequest(authRequest);
        return ctx;
    }

    @Override
    protected ResetPasswordContext doRequest(ResetPasswordContext ctx) {
        var response = wsAuthFeign.registerCustomer(ctx.getAuthRequest());
        if (Boolean.TRUE.equals(response.getBody())) {
            return ctx;
        }
        throw new ResponseStatusException(BAD_REQUEST, "Не удалось сбросить пароль пользователя");
    }
}