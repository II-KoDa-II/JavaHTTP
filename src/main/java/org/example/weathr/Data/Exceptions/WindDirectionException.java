package org.example.weathr.Data.Exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WindDirectionException extends RuntimeException {
    private static final Logger logger = LoggerFactory.getLogger(WindDirectionException.class);
    public WindDirectionException(String message) {
        super(message);
        logger.error(message);
    }
}
