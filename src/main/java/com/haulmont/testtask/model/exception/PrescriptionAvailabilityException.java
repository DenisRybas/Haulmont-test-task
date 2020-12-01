package com.haulmont.testtask.model.exception;

public class PrescriptionAvailabilityException extends Throwable {
    public PrescriptionAvailabilityException() {
    }

    public PrescriptionAvailabilityException(String message) {
        super(message);
    }

    public PrescriptionAvailabilityException(String message, Throwable cause) {
        super(message, cause);
    }

    public PrescriptionAvailabilityException(Throwable cause) {
        super(cause);
    }

    public PrescriptionAvailabilityException(String message,
                                             Throwable cause,
                                             boolean enableSuppression,
                                             boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
