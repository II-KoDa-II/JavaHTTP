package org.example.weathr.Data.Exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReceivingWeatherException extends RuntimeException {
    private static final Logger logger = LoggerFactory.getLogger(ReceivingWeatherException.class);
    public ReceivingWeatherException(String message) {
        super(message);
        logger.error(message);
    }
}
