package org.example.weathr.Data.Exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RetrivingException extends RuntimeException {
    private static final Logger logger = LoggerFactory.getLogger(RetrivingException.class);
    public RetrivingException(String message) {
        super(message);
        logger.error(message);
    }
}
