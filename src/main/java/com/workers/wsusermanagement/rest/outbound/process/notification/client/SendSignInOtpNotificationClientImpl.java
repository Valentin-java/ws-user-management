package com.workers.wsusermanagement.rest.outbound.process.notification.client;

import com.workers.wsusermanagement.bussines.service.signin.context.SignInContext;
import com.workers.wsusermanagement.rest.outbound.feign.WsNotificationServiceFeign;
import com.workers.wsusermanagement.rest.outbound.mapper.NotificationRequestMapper;
import com.workers.wsusermanagement.rest.outbound.process.AbstractProcessFeignClient;
import com.workers.wsusermanagement.rest.outbound.process.notification.interfaces.SendSignInOtpNotificationClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static com.workers.wsusermanagement.rest.outbound.util.CommonFeignUtil.getSpecificMessage;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendSignInOtpNotificationClientImpl
        extends AbstractProcessFeignClient<SignInContext>
        implements SendSignInOtpNotificationClient {

    private final WsNotificationServiceFeign wsNotificationServiceFeign;
    private final NotificationRequestMapper notificationRequestMapper;

    @Override
    protected SignInContext mappingToRequest(SignInContext ctx) {
        var notificationRequest = notificationRequestMapper.toNotificationRequest(ctx);
        ctx.setNotificationRequest(notificationRequest);
        return ctx;
    }

    @Override
    protected SignInContext doRequest(SignInContext ctx) {
        try {
            wsNotificationServiceFeign.sendNotificationMessage(ctx.getNotificationRequest());
            return ctx;
        } catch (Exception ex) {
            throw new ResponseStatusException(BAD_REQUEST, getSpecificMessage(ex));
        }
    }
}
