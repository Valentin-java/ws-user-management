package com.workers.wsusermanagement.rest.outbound.process.reset.client;

import com.workers.wsusermanagement.bussines.service.reset.context.ResetPasswordContext;
import com.workers.wsusermanagement.rest.outbound.feign.WsAuthFeign;
import com.workers.wsusermanagement.rest.outbound.mapper.AuthRequestMapper;
import com.workers.wsusermanagement.rest.outbound.process.reset.interfaces.ResetPasswordProcessFeignClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static com.workers.wsusermanagement.rest.outbound.util.CommonFeignUtil.extractSpecificMessage;
import static com.workers.wsusermanagement.util.CommonConstant.UNEXPECTED_ERROR_MESSAGE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResetPasswordProcessFeignClientImpl implements ResetPasswordProcessFeignClient {

    private final WsAuthFeign wsAuthFeign;
    private final AuthRequestMapper authRequestMapper;

    @Override
    public ResetPasswordContext requestToResetPassword(ResetPasswordContext ctx) {
        log.debug("[requestToResetPassword] Start requestToResetPassword");
        try {
            return Optional.of(ctx)
                    .map(this::mappingToAuthRequest)
                    .map(this::doRequest)
                    .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, UNEXPECTED_ERROR_MESSAGE));
        } catch (FeignException e) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(e.status()), extractSpecificMessage(e));
        }
    }

    private ResetPasswordContext mappingToAuthRequest(ResetPasswordContext ctx) {
        var authRequest = authRequestMapper.toAuthRequest(ctx.getResetPasswordRequest());
        ctx.setAuthRequest(authRequest);
        return ctx;
    }

    private ResetPasswordContext doRequest(ResetPasswordContext ctx) {
        var response = wsAuthFeign.registerCustomer(ctx.getAuthRequest());
        if (Boolean.TRUE.equals(response.getBody())) {
            return ctx;
        }
        throw new ResponseStatusException(BAD_REQUEST, "Не удалось сбросить пароль пользователя");
    }
}
