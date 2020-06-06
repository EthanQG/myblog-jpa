package com.wqg.blog;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @Auther: wqg
 * @Description:
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFondException extends RuntimeException{
    public NotFondException() {
    }

    public NotFondException(String message) {
        super(message);
    }

    public NotFondException(String message, Throwable cause) {
        super(message, cause);
    }
}
