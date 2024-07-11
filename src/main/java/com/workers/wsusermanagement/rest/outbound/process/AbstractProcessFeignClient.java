package com.workers.wsusermanagement.rest.outbound.process;

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
public abstract class AbstractProcessFeignClient<T> {

    public T requestToExecuteByService(T ctx) {
        log.debug("[requestToSendOtp] Start requestToSendOtp");
        try {
            return Optional.of(ctx)
                    .map(this::mappingToRequest)
                    .map(this::doRequest)
                    .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, UNEXPECTED_ERROR_MESSAGE));
        } catch (FeignException e) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(e.status()), extractSpecificMessage(e));
        }
    }

    protected abstract T mappingToRequest(T ctx);

    protected abstract T doRequest(T ctx);

}
