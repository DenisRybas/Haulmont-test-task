package com.haulmont.testtask.model.exception;

/**
 * Исключение, сигнализирующее о том, что у объекта есть
 * {@link com.haulmont.testtask.model.prescription.Prescription},
 * который хранится в БД
 */
public class PrescriptionAvailabilityException extends Throwable {
    public PrescriptionAvailabilityException() {
    }
}
