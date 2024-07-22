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

import static com.workers.wsusermanagement.rest.outbound.util.CommonFeignUtil.getSpecificMessage;
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
        var authRequest = authRequestMapper.toAuthRequest(ctx.getRequest());
        ctx.setAuthRequest(authRequest);
        return ctx;
    }

    @Override
    protected SignUpContext doRequest(SignUpContext ctx) {
        try {
            wsAuthFeign.registerCustomer(ctx.getAuthRequest());
            return ctx;
        } catch (Exception ex) {
            throw new ResponseStatusException(BAD_REQUEST, getSpecificMessage(ex));
        }
    }
}
