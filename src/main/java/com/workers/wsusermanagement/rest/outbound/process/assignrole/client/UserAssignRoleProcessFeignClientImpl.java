package com.workers.wsusermanagement.rest.outbound.process.assignrole.client;

import com.workers.wsusermanagement.bussines.service.signup.context.SignUpContext;
import com.workers.wsusermanagement.rest.outbound.feign.WsAuthFeign;
import com.workers.wsusermanagement.rest.outbound.mapper.AuthRequestMapper;
import com.workers.wsusermanagement.rest.outbound.process.AbstractProcessFeignClient;
import com.workers.wsusermanagement.rest.outbound.process.assignrole.interfaces.UserAssignRoleProcessFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserAssignRoleProcessFeignClientImpl
        extends AbstractProcessFeignClient<SignUpContext>
        implements UserAssignRoleProcessFeignClient {

    private final WsAuthFeign wsAuthFeign;
    private final AuthRequestMapper authRequestMapper;

    @Override
    protected SignUpContext mappingToRequest(SignUpContext ctx) {
        var authRequest = authRequestMapper.toAssignRoleRequest(ctx.getSignUpRequest());
        ctx.setAssignRoleRequest(authRequest);
        return ctx;
    }

    @Override
    protected SignUpContext doRequest(SignUpContext ctx) {
        var response = wsAuthFeign.assignRole(ctx.getAssignRoleRequest());
        if (response.getStatusCode().is2xxSuccessful()) {
            return ctx;
        }
        throw new ResponseStatusException(BAD_REQUEST, "Не удалось назначить роль для  пользователя");
    }
}
