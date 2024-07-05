package com.workers.wsusermanagement.bussines.service.validation.signup;

import com.workers.wsusermanagement.rest.inbound.dto.SignUpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
public class SignUpChainValidatorImpl implements SignUpValidationService {

    private final List<SignUpValidator> validators;

    public SignUpChainValidatorImpl(List<SignUpValidator> validators) {
        this.validators = validators.stream()
                .sorted(Comparator.comparing(SignUpValidator::getOrder))
                .collect(Collectors.toList());
    }

    @Override
    public SignUpRequest validate(SignUpRequest request) {
        List<String> errors = new ArrayList<>();
        validators.forEach(v -> v.validate(request, errors));
        if (!errors.isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST, errors.stream().collect(Collectors.joining(System.lineSeparator())));
        }
        return request;
    }
}
