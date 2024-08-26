package com.uevitondev.deliverybackend.domain.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class CookieService {
    private final Logger log = LoggerFactory.getLogger(CookieService.class);


    public Cookie createCookie(
            String cookieName,
            String cookieValue,
            String cookiePath,
            int cookieMaxAge,
            boolean cookieHttpOnly,
            boolean cookieSecure
    ) {

        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setPath(cookiePath);
        cookie.setMaxAge(cookieMaxAge);
        cookie.setHttpOnly(cookieHttpOnly);
        cookie.setSecure(cookieSecure);
        log.info("[CookieService:createCookie] Cookie created, name: {}", cookieName);
        return cookie;
    }


    public String extractCookieValueFromRequestByCookieName(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        String cookieValue = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    cookieValue = cookie.getValue();
                    break;
                }
            }
        }
        if (cookieValue == null) {
            throw new AccessDeniedException("access denied");
        }
        log.info("[CookieService:extractCookieValueFromRequestByCookieName] Cookie value has ben extract: {}", cookieValue);
        return cookieValue;
    }


}
