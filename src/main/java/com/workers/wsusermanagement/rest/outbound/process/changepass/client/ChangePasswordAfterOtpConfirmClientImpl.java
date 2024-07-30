package com.workers.wsusermanagement.rest.outbound.process.changepass.client;

import com.workers.wsusermanagement.bussines.service.setpass.context.ChangePasswordContext;
import com.workers.wsusermanagement.rest.outbound.feign.WsAuthFeign;
import com.workers.wsusermanagement.rest.outbound.mapper.AuthRequestMapper;
import com.workers.wsusermanagement.rest.outbound.process.AbstractProcessFeignClient;
import com.workers.wsusermanagement.rest.outbound.process.changepass.interfaces.ChangePasswordAfterOtpConfirmClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static com.workers.wsusermanagement.rest.outbound.util.CommonFeignUtil.getSpecificMessage;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChangePasswordAfterOtpConfirmClientImpl
        extends AbstractProcessFeignClient<ChangePasswordContext>
        implements ChangePasswordAfterOtpConfirmClient {

    private final WsAuthFeign wsAuthFeign;
    private final AuthRequestMapper authRequestMapper;

    @Override
    protected ChangePasswordContext mappingToRequest(ChangePasswordContext ctx) {
        var authRequest = authRequestMapper.toAuthRequest(ctx);
        ctx.setAuthRequest(authRequest);
        return ctx;
    }

    @Override
    protected ChangePasswordContext doRequest(ChangePasswordContext ctx) {
        try {
            wsAuthFeign.requestToChangePassword(ctx.getAuthRequest());
            return ctx;
        } catch (Exception ex) {
            throw new ResponseStatusException(BAD_REQUEST, getSpecificMessage(ex));
        }
    }
}
