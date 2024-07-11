package com.workers.wsusermanagement.rest.outbound.process.notification.client;

import com.workers.wsusermanagement.bussines.service.reset.context.ResetPasswordContext;
import com.workers.wsusermanagement.bussines.service.signup.context.SignUpContext;
import com.workers.wsusermanagement.rest.outbound.feign.WsAuthFeign;
import com.workers.wsusermanagement.rest.outbound.feign.WsNotificationServiceFeign;
import com.workers.wsusermanagement.rest.outbound.mapper.AuthRequestMapper;
import com.workers.wsusermanagement.rest.outbound.mapper.NotificationRequestMapper;
import com.workers.wsusermanagement.rest.outbound.process.notification.interfaces.WsNotificationServiceFeignClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static com.workers.wsusermanagement.rest.outbound.util.CommonFeignUtil.extractSpecificMessage;
import static com.workers.wsusermanagement.util.CommonConstant.UNEXPECTED_ERROR_MESSAGE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Component
@RequiredArgsConstructor
public class WsNotificationServiceFeignClientImpl implements WsNotificationServiceFeignClient {

    private final WsNotificationServiceFeign wsNotificationServiceFeign;
    private final NotificationRequestMapper notificationRequestMapper;

    @Override
    public ResetPasswordContext requestToSendOtp(ResetPasswordContext ctx) {
        log.debug("[requestToRegistryCustomer] Start requestToRegistryCustomer");
        try {
            return Optional.of(ctx)
                    .map(this::mappingToNotificationRequest)
                    .map(this::doNotificationRequest)
                    .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, UNEXPECTED_ERROR_MESSAGE));
        } catch (FeignException e) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(e.status()), extractSpecificMessage(e));
        }
    }

    private ResetPasswordContext mappingToNotificationRequest(ResetPasswordContext ctx) {
        var notificationRequest = notificationRequestMapper.toNotificationRequest(ctx);
        ctx.setNotificationRequest(notificationRequest);
        return ctx;
    }

    private ResetPasswordContext doNotificationRequest(ResetPasswordContext ctx) {
        var response = wsNotificationServiceFeign.sendNotificationMessage(ctx.getNotificationRequest());
        if (Boolean.TRUE.equals(response.getBody())) {
            return ctx;
        }
        throw new ResponseStatusException(BAD_REQUEST, "Не отправить временный пароль пользователя");
    }
}
