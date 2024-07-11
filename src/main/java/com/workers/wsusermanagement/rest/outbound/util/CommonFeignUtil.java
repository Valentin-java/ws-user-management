package com.workers.wsusermanagement.rest.outbound.util;

import feign.FeignException;

public class CommonFeignUtil {

    public static String extractSpecificMessage(FeignException e) {
        return e.getMessage()
                .split(":")[3]
                .replaceAll("\\[", "")
                .replaceAll("\\]", "")
                .trim();
    }
}
