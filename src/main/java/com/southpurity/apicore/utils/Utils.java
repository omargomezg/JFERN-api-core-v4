package com.southpurity.apicore.utils;

import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Log4j2
public final class Utils {

    private Utils() {
    }

    public static String getIpAddress(HttpServletRequest httpServletRequest) {
        String ipAddress = httpServletRequest.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = httpServletRequest.getRemoteAddr();
        }
        if (ipAddress.contains(",")) {
            ipAddress = ipAddress.split(",")[0];
        }
        log.info("IP Address: {}", ipAddress);
        return ipAddress;
    }

    public static String getUserAgent(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader("User-Agent");
    }

    public static String generateCode(int length) {
        return UUID.randomUUID().toString().replace("-", "").substring(0, length);
    }
}
