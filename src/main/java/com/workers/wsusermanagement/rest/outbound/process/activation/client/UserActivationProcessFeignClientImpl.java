package com.workers.wsusermanagement.rest.outbound.process.activation.client;

import com.workers.wsusermanagement.bussines.service.signup.context.VerifySignUpContext;
import com.workers.wsusermanagement.rest.outbound.feign.WsAuthFeign;
import com.workers.wsusermanagement.rest.outbound.mapper.AuthRequestMapper;
import com.workers.wsusermanagement.rest.outbound.process.AbstractProcessFeignClient;
import com.workers.wsusermanagement.rest.outbound.process.activation.interfaces.UserActivationProcessFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static com.workers.wsusermanagement.rest.outbound.util.CommonFeignUtil.getSpecificMessage;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserActivationProcessFeignClientImpl
        extends AbstractProcessFeignClient<VerifySignUpContext>
        implements UserActivationProcessFeignClient {

    private final WsAuthFeign wsAuthFeign;
    private final AuthRequestMapper authRequestMapper;

    @Override
    protected VerifySignUpContext mappingToRequest(VerifySignUpContext ctx) {
        if (ctx.getAuthRequest() != null) {
            return ctx;
        }
        var authRequest = authRequestMapper.toAuthRequest(ctx);
        ctx.setAuthRequest(authRequest);
        return ctx;
    }

    @Override
    protected VerifySignUpContext doRequest(VerifySignUpContext ctx) {
        try {
            wsAuthFeign.activationCustomer(ctx.getAuthRequest());
            return ctx;
        } catch (Exception ex) {
            throw new ResponseStatusException(BAD_REQUEST, getSpecificMessage(ex));
        }
    }
}
