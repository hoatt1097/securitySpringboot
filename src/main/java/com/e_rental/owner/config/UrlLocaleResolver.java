package com.e_rental.owner.config;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.LocaleResolver;

public class UrlLocaleResolver implements LocaleResolver {

    @Override
    public Locale resolveLocale(HttpServletRequest request) {

        String localePrefix = request.getParameter("lang");

        Locale locale = null;

        // English
        if (localePrefix != null && localePrefix.startsWith("en")) {
            locale = Locale.ENGLISH;
        }
        // Japanese
        if (localePrefix != null && localePrefix.startsWith("ja")) {
            locale = Locale.JAPANESE;
        }
        // Vietnamese
        if (localePrefix != null && localePrefix.startsWith("vi")) {
            locale = new Locale("vi", "VN");
        }

        if (locale == null) {
            locale = Locale.ENGLISH;
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        // Nothing
    }

}