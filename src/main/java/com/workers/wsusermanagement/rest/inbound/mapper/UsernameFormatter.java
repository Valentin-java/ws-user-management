package com.workers.wsusermanagement.rest.inbound.mapper;

import com.workers.wsusermanagement.config.mapper.MapperConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(config = MapperConfiguration.class)
public interface UsernameFormatter {

    @Named("FormatPhoneNumber")
    default String formatPhoneNumber(String phoneNumber) {
        phoneNumber = phoneNumber.replaceAll(" ", "");

        if (phoneNumber.startsWith("+7")) {
            phoneNumber = phoneNumber.substring(2);
        }
        return phoneNumber;
    }
}
