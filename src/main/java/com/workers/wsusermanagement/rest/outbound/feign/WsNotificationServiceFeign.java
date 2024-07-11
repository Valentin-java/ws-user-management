package com.workers.wsusermanagement.rest.outbound.feign;

import com.workers.wsusermanagement.rest.outbound.model.NotificationMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "WsNotificationServiceFeign", url = "${feign.ws-notification.url}", path = "/v1")
public interface WsNotificationServiceFeign {

    @PostMapping(value = "/workers/notice/sms")
    ResponseEntity<Boolean> sendNotificationMessage(@RequestBody NotificationMessage request);
}
