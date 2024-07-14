package com.workers.wsusermanagement.rest.outbound.process.login.client;

import com.workers.wsusermanagement.bussines.service.signin.context.SignInContext;
import com.workers.wsusermanagement.bussines.service.signin.model.SignInResponse;
import com.workers.wsusermanagement.rest.outbound.feign.WsAuthFeign;
import com.workers.wsusermanagement.rest.outbound.mapper.AuthRequestMapper;
import com.workers.wsusermanagement.rest.outbound.process.AbstractProcessFeignClient;
import com.workers.wsusermanagement.rest.outbound.process.login.interfaces.CustomerLoginProcessFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerLoginProcessFeignClientImpl
        extends AbstractProcessFeignClient<SignInContext>
        implements CustomerLoginProcessFeignClient {

    private final WsAuthFeign wsAuthFeign;
    private final AuthRequestMapper authRequestMapper;

    @Override
    protected SignInContext mappingToRequest(SignInContext ctx) {
        var authRequest = authRequestMapper.toAuthRequest(ctx.getRequest());
        ctx.setAuthRequest(authRequest);
        return ctx;
    }

    @Override
    protected SignInContext doRequest(SignInContext ctx) {
        var response = wsAuthFeign.createAuthenticationToken(ctx.getAuthRequest());
        if (response.getStatusCode().is2xxSuccessful()) {
            var responseBody = response.getBody() != null ? response.getBody() : null;
            var accessToken = responseBody.accessToken();
            var refreshToken = responseBody.refreshToken();
            var signInResponse = new SignInResponse(ctx.getRequest().phoneNumber(), accessToken, refreshToken);
            return ctx.setSignInResponse(signInResponse);
        }
        throw new ResponseStatusException(BAD_REQUEST, "Не удалось залогинить пользователя");
    }
}
