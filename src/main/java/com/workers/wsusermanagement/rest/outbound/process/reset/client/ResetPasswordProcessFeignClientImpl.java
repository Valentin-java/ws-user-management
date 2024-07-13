package com.workers.wsusermanagement.rest.outbound.process.reset.client;

import com.workers.wsusermanagement.bussines.service.confirmotp.context.ConfirmationOtpContext;
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
        extends AbstractProcessFeignClient<ConfirmationOtpContext>
        implements ResetPasswordProcessFeignClient {

    private final WsAuthFeign wsAuthFeign;
    private final AuthRequestMapper authRequestMapper;

    @Override
    protected ConfirmationOtpContext mappingToRequest(ConfirmationOtpContext ctx) {
        var authRequest = authRequestMapper.toAuthRequest(ctx.getRequest());
        ctx.setAuthRequest(authRequest);
        return ctx;
    }

    @Override
    protected ConfirmationOtpContext doRequest(ConfirmationOtpContext ctx) {
        var response = wsAuthFeign.registerCustomer(ctx.getAuthRequest());
        if (Boolean.TRUE.equals(response.getBody())) {
            return ctx;
        }
        throw new ResponseStatusException(BAD_REQUEST, "Не удалось сбросить пароль пользователя");
    }
}
