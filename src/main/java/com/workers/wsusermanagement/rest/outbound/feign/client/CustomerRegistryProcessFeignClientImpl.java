package com.workers.wsusermanagement.rest.outbound.feign.client;

import com.workers.wsusermanagement.bussines.service.signup.context.SignUpContext;
import com.workers.wsusermanagement.bussines.service.validation.signup.SignUpValidationService;
import com.workers.wsusermanagement.rest.outbound.feign.controller.WsAuthFeign;
import com.workers.wsusermanagement.rest.outbound.mapper.AuthRequestMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static com.workers.wsusermanagement.util.CommonConstant.UNEXPECTED_ERROR_MESSAGE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerRegistryProcessFeignClientImpl implements CustomerRegistryProcessFeignClient {

    private final WsAuthFeign wsAuthFeign;
    private final SignUpValidationService validationService;
    private final AuthRequestMapper authRequestMapper;

    @Override
    public SignUpContext requestToRegistryCustomer(SignUpContext ctx) {
        log.debug("[requestToRegistryCustomer] Start requestToRegistryCustomer");
        try {
            return Optional.of(ctx)
                    .map(this::validateRequest)
                    .map(this::mappingToAuthRequest)
                    .map(this::doRequestToRegistry)
                    .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, UNEXPECTED_ERROR_MESSAGE));
        } catch (FeignException e) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(e.status()), extractSpecificMessage(e));
        }
    }

    private SignUpContext validateRequest(SignUpContext ctx) {
        validationService.validate(ctx.getSignUpRequest());
        return ctx;
    }

    private SignUpContext mappingToAuthRequest(SignUpContext ctx) {
        var authRequest = authRequestMapper.toAuthRequest(ctx.getSignUpRequest());
        ctx.setAuthRequest(authRequest);
        return ctx;
    }

    private SignUpContext doRequestToRegistry(SignUpContext ctx) {
        var response = wsAuthFeign.registerCustomer(ctx.getAuthRequest());
        if (Boolean.TRUE.equals(response.getBody())) {
            return ctx;
        }
        throw new ResponseStatusException(BAD_REQUEST, "Не удалось зарегистрировать пользователя");
    }

    private String extractSpecificMessage(FeignException e) {
        return e.getMessage()
                .split(":")[3]
                .replaceAll("\\[", "")
                .replaceAll("\\]", "")
                .trim();
    }

    @Override
    public SignUpContext requestToAssignRole(SignUpContext ctx) {
        log.debug("[requestToAssignRole] Start requestToAssignRole");
        try {
            return Optional.of(ctx)
                    .map(this::mappingToAssignRoleRequest)
                    .map(this::doRequestToAssignRole)
                    .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, UNEXPECTED_ERROR_MESSAGE));
        } catch (FeignException e) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(e.status()), extractSpecificMessage(e));
        }
    }

    private SignUpContext mappingToAssignRoleRequest(SignUpContext ctx) {
        var authRequest = authRequestMapper.toAssignRoleRequest(ctx.getSignUpRequest());
        ctx.setAssignRoleRequest(authRequest);
        return ctx;
    }

    private SignUpContext doRequestToAssignRole(SignUpContext ctx) {
        var response = wsAuthFeign.assignRole(ctx.getAssignRoleRequest());
        if (response.getStatusCode().is2xxSuccessful()) {
            return ctx;
        }
        throw new ResponseStatusException(BAD_REQUEST, "Не удалось зарегистрировать пользователя");
    }

    @Override
    public SignUpContext requestToActivationCustomer(SignUpContext ctx) {
        log.debug("[requestToActivationCustomer] Start requestToActivationCustomer");
        try {
            return Optional.of(ctx)
                    .map(this::doRequestToActivationCustomer)
                    .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, UNEXPECTED_ERROR_MESSAGE));
        } catch (FeignException e) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(e.status()), extractSpecificMessage(e));
        }
    }

    private SignUpContext doRequestToActivationCustomer(SignUpContext ctx) {
        var response = wsAuthFeign.registerCustomer(ctx.getAuthRequest());
        if (Boolean.TRUE.equals(response.getBody())) {
            return ctx;
        }
        throw new ResponseStatusException(BAD_REQUEST, "Не удалось активировать пользователя");
    }

}
