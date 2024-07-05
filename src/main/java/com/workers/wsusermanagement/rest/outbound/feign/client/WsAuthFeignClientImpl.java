package com.workers.wsusermanagement.rest.outbound.feign.client;

import com.workers.wsusermanagement.bussines.service.validation.signup.SignUpValidationService;
import com.workers.wsusermanagement.rest.inbound.dto.SignUpRequest;
import com.workers.wsusermanagement.rest.outbound.feign.controller.WsAuthFeign;
import com.workers.wsusermanagement.rest.outbound.mapper.AuthRequestMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static com.workers.wsusermanagement.util.CommonConstant.UNEXPECTED_ERROR_MESSAGE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Component
@RequiredArgsConstructor
public class WsAuthFeignClientImpl implements WsAuthFeignClient {

    private final WsAuthFeign wsAuthFeign;
    private final SignUpValidationService validationService;
    private final AuthRequestMapper authRequestMapper;

    @Override
    public Boolean requestToRegistryCustomer(SignUpRequest request) {
        log.debug("[requestToRegistryCustomer] Start registerCustomerClient");
        try {
            return Optional.of(request)
                    .map(validationService::validate)
                    .map(authRequestMapper::toRest)
                    .map(wsAuthFeign::registerCustomer)
                    .map(this::extractResponse)
                    .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, UNEXPECTED_ERROR_MESSAGE));
        } catch (FeignException e) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(e.status()), extractSpecificMessage(e));
        }
    }

    private Boolean extractResponse(ResponseEntity<Boolean> response) {
        return response.getBody();
    }

    private String extractSpecificMessage(FeignException e) {
        return e.getMessage()
                .split(":")[3]
                .replaceAll("\\[", "")
                .replaceAll("\\]", "")
                .trim();
    }
}
