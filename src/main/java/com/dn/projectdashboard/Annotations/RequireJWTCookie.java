package com.dn.projectdashboard.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireJWTCookie {
    String[] roles() default {};
    String cookieName() default "DNPD_AUTH_TOKEN";
}