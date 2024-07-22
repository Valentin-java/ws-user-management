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

    public static String getSpecificMessage(Exception ex) {
        var lastOne = ex.getMessage().split(":").length - 1;
        return ex.getMessage().split(":")[lastOne].trim()
                .replaceAll("\\]", "")
                .replaceAll("\\[", "")
                .replace("\"", "");
    }
}
