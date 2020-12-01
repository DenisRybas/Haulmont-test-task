package com.haulmont.testtask.model;

import com.haulmont.testtask.model.exception.PrescriptionAvailabilityException;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    T get(Long id);
    List<T> getAll();
    void save(T t);
    void update(T old, T updated);
    void delete(T t) throws PrescriptionAvailabilityException;
}
