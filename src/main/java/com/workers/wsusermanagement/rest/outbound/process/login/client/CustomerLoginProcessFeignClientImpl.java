package com.workers.wsusermanagement.rest.outbound.process.login.client;

import com.workers.wsusermanagement.bussines.service.signin.context.SignInContext;
import com.workers.wsusermanagement.bussines.service.signin.model.SignInResponse;
import com.workers.wsusermanagement.rest.outbound.feign.WsAuthFeign;
import com.workers.wsusermanagement.rest.outbound.mapper.AuthRequestMapper;
import com.workers.wsusermanagement.rest.outbound.process.login.interfaces.CustomerLoginProcessFeignClient;
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
public class CustomerLoginProcessFeignClientImpl implements CustomerLoginProcessFeignClient {

    private final WsAuthFeign wsAuthFeign;
    private final AuthRequestMapper authRequestMapper;

    @Override
    public SignInContext requestToLoginCustomer(SignInContext ctx) {
        log.debug("[requestToLoginCustomer] Start requestToLoginCustomer");
        try {
            return Optional.of(ctx)
                    .map(this::mappingToAuthRequest)
                    .map(this::doRequestToRegistry)
                    .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, UNEXPECTED_ERROR_MESSAGE));
        } catch (FeignException e) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(e.status()), extractSpecificMessage(e));
        }
    }

    private SignInContext mappingToAuthRequest(SignInContext ctx) {
        var authRequest = authRequestMapper.toAuthRequest(ctx.getSignInRequest());
        ctx.setAuthRequest(authRequest);
        return ctx;
    }

    private SignInContext doRequestToRegistry(SignInContext ctx) {
        var response = wsAuthFeign.activationCustomer(ctx.getAuthRequest());
        if (response.getStatusCode().is2xxSuccessful()) {
            var responseBody = response.getBody() != null ? response.getBody() : null;
            var accessToken = responseBody.accessToken();
            var refreshToken = responseBody.refreshToken();
            var signInResponse = new SignInResponse(ctx.getSignInRequest().phoneNumber(), accessToken, refreshToken);
            return ctx.setSignInResponse(signInResponse);
        }
        throw new ResponseStatusException(BAD_REQUEST, "Не удалось залогинить пользователя");
    }

    private String extractSpecificMessage(FeignException e) {
        return e.getMessage()
                .split(":")[3]
                .replaceAll("\\[", "")
                .replaceAll("\\]", "")
                .trim();
    }
}
