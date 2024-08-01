package com.workers.wsusermanagement.rest.outbound.process.login.client;

import com.workers.wsusermanagement.bussines.service.signin.context.SignInByPassContext;
import com.workers.wsusermanagement.bussines.service.signin.model.SignInResponse;
import com.workers.wsusermanagement.rest.outbound.feign.WsAuthFeign;
import com.workers.wsusermanagement.rest.outbound.mapper.AuthRequestMapper;
import com.workers.wsusermanagement.rest.outbound.process.AbstractProcessFeignClient;
import com.workers.wsusermanagement.rest.outbound.process.login.interfaces.CustomerLoginByPasswordProcessClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static com.workers.wsusermanagement.rest.outbound.util.CommonFeignUtil.getSpecificMessage;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerLoginByPasswordProcessClientImpl
        extends AbstractProcessFeignClient<SignInByPassContext>
        implements CustomerLoginByPasswordProcessClient {

    private final WsAuthFeign wsAuthFeign;
    private final AuthRequestMapper authRequestMapper;

    @Override
    protected SignInByPassContext mappingToRequest(SignInByPassContext ctx) {
        if (ctx.getAuthRequest() == null) {
            var authRequest = authRequestMapper.toAuthRequest(ctx);
            ctx.setAuthRequest(authRequest);
        }
        return ctx;
    }

    @Override
    protected SignInByPassContext doRequest(SignInByPassContext ctx) {
        try {
            var response = wsAuthFeign.createAuthenticationToken(ctx.getAuthRequest());
            var responseBody = response.getBody() != null ? response.getBody() : null;
            return ctx.setSignInResponse(new SignInResponse(responseBody.accessToken()));
        } catch (Exception ex) {
            throw new ResponseStatusException(UNAUTHORIZED, getSpecificMessage(ex));
        }
    }
}
