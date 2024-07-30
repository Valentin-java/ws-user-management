package com.workers.wsusermanagement.rest.outbound.process.login.client;

import com.workers.wsusermanagement.bussines.service.signin.model.SignInResponse;
import com.workers.wsusermanagement.bussines.service.signup.context.VerifySignUpContext;
import com.workers.wsusermanagement.rest.outbound.feign.WsAuthFeign;
import com.workers.wsusermanagement.rest.outbound.mapper.AuthRequestMapper;
import com.workers.wsusermanagement.rest.outbound.process.AbstractProcessFeignClient;
import com.workers.wsusermanagement.rest.outbound.process.login.interfaces.CustomerLoginAfterRegistryClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static com.workers.wsusermanagement.rest.outbound.util.CommonFeignUtil.getSpecificMessage;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerLoginAfterRegistryClientImpl
        extends AbstractProcessFeignClient<VerifySignUpContext>
        implements CustomerLoginAfterRegistryClient {

    private final WsAuthFeign wsAuthFeign;
    private final AuthRequestMapper authRequestMapper;

    @Override
    protected VerifySignUpContext mappingToRequest(VerifySignUpContext ctx) {
        if (ctx.getAuthRequest() == null) {
            var authRequest = authRequestMapper.toAuthRequest(ctx);
            ctx.setAuthRequest(authRequest);
        }
        return ctx;
    }

    @Override
    protected VerifySignUpContext doRequest(VerifySignUpContext ctx) {
        try {
            var response = wsAuthFeign.createAuthenticationToken(ctx.getAuthRequest());
            var responseBody = response.getBody() != null ? response.getBody() : null;
            return ctx.setSignInResponse(new SignInResponse(responseBody.accessToken()));
        } catch (Exception ex) {
            throw new ResponseStatusException(UNAUTHORIZED, getSpecificMessage(ex));
        }
    }
}
