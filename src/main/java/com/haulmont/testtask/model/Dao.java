package com.haulmont.testtask.model;

import com.haulmont.testtask.model.exception.PrescriptionAvailabilityException;

import java.util.List;

/**
 * Интерфейс, нужный для создания Dao конкретной сущности
 * @param <T> - сущность
 */
public interface Dao<T> {
    /**
     * Метод, позволяющий получить значение сущности по её id
     *
     * @param id - уникальный идентификатор сущности
     * @return сущность
     */
    T get(Long id);

    /**
     * Метод, позволяющий получить значения всех сущностей, хранящихся в БД
     *
     * @return {@link List} сущностей
     * @see List
     */
    List<T> getAll();

    /**
     * Метод, позволяющий сохранить сущность в БД
     *
     * @param t - сущность
     */
    void save(T t);

    /**
     * Метод, позволяющий обновить сущность в БД
     *
     * @param old - сущность, которая будет обновлена
     * @param updated - обновлённая сущность
     */
    void update(T old, T updated);

    /**
     * Метод, позволяющий удалить сущность из БД
     *
     * @param t - сущность
     * @throws PrescriptionAvailabilityException - исключение, бросаемое
     * в случае наличии у сущности хотя бы одного рецепта
     * @see PrescriptionAvailabilityException
     */
    void delete(T t) throws PrescriptionAvailabilityException;
}
