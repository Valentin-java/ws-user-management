package com.workers.wsusermanagement.bussines.service.validation.signup.chain;

import com.workers.wsusermanagement.bussines.service.validation.signup.SignUpValidator;
import com.workers.wsusermanagement.rest.dto.SignUpRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.workers.wsusermanagement.util.SequenceOfValidation.PHONE_NUMBER_VALIDATOR;

@Component
@ConditionalOnProperty(name = "validator.phone-number-validator.enable", matchIfMissing = true, havingValue = "true")
public class PhoneNumberValidator implements SignUpValidator {

    @Override
    public Integer getOrder() {
        return PHONE_NUMBER_VALIDATOR;
    }

    /**
     * Validates a Russian phone number after cleaning it from spaces, dashes, and brackets.
     * Throws an IllegalArgumentException if the number is invalid.
     *
     * @param request The phone number to validate.
     * @return true if the phone number is valid.
     * @throws IllegalArgumentException if the phone number is invalid.
     */
    @Override
    public void validate(SignUpRequest request, List<String> errors) {
        if (request.phoneNumber() == null) {
            errors.add("Phone number cannot be null");
        }

        String cleanedNumber = request.phoneNumber().replaceAll("[ -()]", "");

        // Regex to match Russian phone number pattern +79xxxxxxxxx
        String regex = "\\+79\\d{9}";

        // Check if the cleaned number matches the pattern
        if (!cleanedNumber.matches(regex)) {
            errors.add("Invalid phone number format");
        }
    }
}
