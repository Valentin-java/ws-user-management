package com.workers.wsusermanagement.rest.outbound.process.assignrole.client;

import com.workers.wsusermanagement.bussines.service.signup.context.VerifySignUpContext;
import com.workers.wsusermanagement.rest.outbound.feign.WsAuthFeign;
import com.workers.wsusermanagement.rest.outbound.model.AssignRoleRequest;
import com.workers.wsusermanagement.rest.outbound.process.AbstractProcessFeignClient;
import com.workers.wsusermanagement.rest.outbound.process.assignrole.interfaces.UserAssignRoleProcessFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static com.workers.wsusermanagement.rest.outbound.util.CommonFeignUtil.getSpecificMessage;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserAssignRoleProcessFeignClientImpl
        extends AbstractProcessFeignClient<VerifySignUpContext>
        implements UserAssignRoleProcessFeignClient {

    private final WsAuthFeign wsAuthFeign;

    @Override
    protected VerifySignUpContext mappingToRequest(VerifySignUpContext ctx) {
        ctx.setAssignRoleRequest(new AssignRoleRequest(
                ctx.getUserProfile().getUsername(),
                ctx.getAssignRoleRequest().role()));
        return ctx;
    }

    @Override
    protected VerifySignUpContext doRequest(VerifySignUpContext ctx) {
        try {
            wsAuthFeign.assignRole(ctx.getAssignRoleRequest());
            return ctx;
        } catch (Exception ex) {
            throw new ResponseStatusException(BAD_REQUEST, getSpecificMessage(ex));
        }
    }
}
