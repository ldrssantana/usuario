package com.leandro.usuario.infrastructure.exceptions;

import javax.naming.AuthenticationException;

public class UnauthorizedException  extends AuthenticationException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable throwable) {
        super(message);
    }

}
