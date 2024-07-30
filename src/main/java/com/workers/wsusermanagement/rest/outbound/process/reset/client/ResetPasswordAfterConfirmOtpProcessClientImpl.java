package com.workers.wsusermanagement.rest.outbound.process.reset.client;

import com.workers.wsusermanagement.bussines.service.confirmotp.context.ConfirmationOtpContext;
import com.workers.wsusermanagement.rest.outbound.feign.WsAuthFeign;
import com.workers.wsusermanagement.rest.outbound.mapper.AuthRequestMapper;
import com.workers.wsusermanagement.rest.outbound.process.AbstractProcessFeignClient;
import com.workers.wsusermanagement.rest.outbound.process.reset.interfaces.ResetPasswordAfterConfirmOtpProcessClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static com.workers.wsusermanagement.rest.outbound.util.CommonFeignUtil.getSpecificMessage;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResetPasswordAfterConfirmOtpProcessClientImpl
        extends AbstractProcessFeignClient<ConfirmationOtpContext>
        implements ResetPasswordAfterConfirmOtpProcessClient {

    private final WsAuthFeign wsAuthFeign;
    private final AuthRequestMapper authRequestMapper;

    @Override
    protected ConfirmationOtpContext mappingToRequest(ConfirmationOtpContext ctx) {
        var authRequest = authRequestMapper.toAuthRequest(ctx);
        ctx.setAuthRequest(authRequest);
        return ctx;
    }

    @Override
    protected ConfirmationOtpContext doRequest(ConfirmationOtpContext ctx) {
        try {
            wsAuthFeign.requestToResetPassword(ctx.getAuthRequest());
            return ctx;
        } catch (Exception ex) {
            throw new ResponseStatusException(BAD_REQUEST, getSpecificMessage(ex));
        }
    }
}
