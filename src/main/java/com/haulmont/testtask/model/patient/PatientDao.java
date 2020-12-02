package com.haulmont.testtask.model.patient;

import com.haulmont.testtask.model.Dao;
import com.haulmont.testtask.model.exception.PrescriptionAvailabilityException;
import com.haulmont.testtask.util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, реализующий CRUD - методы для сущности {@link Patient},
 * нужен для общения с БД
 *
 * @see Patient
 */
public class PatientDao implements Dao<Patient> {

    /**
     * Метод для получения пациента из БД по его id. Если отлавливается
     * SQLException, то в консоль выводится его сообщение
     *
     * @param id - уникальный идентификатор пациента
     * @return возвращает пациента, полученного из БД по id
     * @see SQLException
     */
    @Override
    public Patient get(Long id) {
        String sql = "SELECT * FROM Patient WHERE id = ?;";
        Patient patient = new Patient();
        try (PreparedStatement statement = ConnectionUtil.getConnection()
                .prepareStatement(sql)) {

            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            patient.setId(resultSet.getLong("id"));
            patient.setName(resultSet.getString("name"));
            patient.setSurname(resultSet.getString("surname"));
            patient.setPatronymic(resultSet.getString("patronymic"));
            patient.setPhoneNumber(resultSet.getString("phone_number"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return patient;
    }

    /**
     * Метод для получения всех пациентов из БД. Если отлавливается
     * SQLException, то в консоль выводится его сообщение
     *
     * @return возвращает {@link List} пациентов, полученных из БД
     * @see List
     * @see SQLException
     */
    @Override
    public List<Patient> getAll() {
        String sql = "SELECT * FROM Patient;";
        List<Patient> list = new ArrayList<>();
        try (PreparedStatement statement = ConnectionUtil.getConnection()
                .prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Patient patient = new Patient();
                patient.setId(resultSet.getLong("id"));
                patient.setName(resultSet.getString("name"));
                patient.setSurname(resultSet.getString("surname"));
                patient.setPatronymic(resultSet.getString("patronymic"));
                patient.setPhoneNumber(resultSet.getString("phone_number"));
                list.add(patient);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    /**
     * Метод для сохранения пациента в БД. Если отлавливается
     * SQLException, то в консоль выводится его сообщение
     *
     * @param patient - пациент для сохранения
     * @see SQLException
     */
    @Override
    public void save(Patient patient) {
        String sql = "INSERT INTO Patient(name, surname, patronymic, phone_number)"
                + "VALUES(?, ?, ?, ?)";
        try (PreparedStatement statement = ConnectionUtil.getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, patient.getName());
            statement.setString(2, patient.getSurname());
            statement.setString(3, patient.getPatronymic());
            statement.setString(4, patient.getPhoneNumber());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    patient.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating patient failed, " +
                            "no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Метод для обновления уже существующего в
     * БД пациента. Если отлавливается SQLException,
     * то в консоль выводится его сообщение
     *
     * @param oldPatient - пациент, который будет обновлён
     * @param newPatient - пациент, который нужен
     *                   для обновления oldPatient
     * @see SQLException
     */
    @Override
    public void update(Patient oldPatient, Patient newPatient) {
        String sql = "UPDATE Patient set name = ?, surname = ?, patronymic = ?, " +
                "phone_number = ? where id = ?";
        try (PreparedStatement statement = ConnectionUtil.getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, newPatient.getName());
            statement.setString(2, newPatient.getSurname());
            statement.setString(3, newPatient.getPatronymic());
            statement.setString(4, newPatient.getPhoneNumber());
            statement.setLong(5, oldPatient.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Метод для удаления пациента из БД. Если отлавливается
     * SQLException и его причиной было удаление пациента, у которого есть
     * {@link com.haulmont.testtask.model.prescription.Prescription},
     * то пробрасывается {@link PrescriptionAvailabilityException}
     *
     * @param patient - пациент, который будет удалён из БД
     * @throws PrescriptionAvailabilityException - исключение, сигнализирующее
     *                                           о том, что пациент,
     *                                           которого пользователь
     *                                           хочет удалить из бд,
     *                                           имеет рецепты
     * @see SQLException
     * @see PrescriptionAvailabilityException
     * @see com.haulmont.testtask.model.prescription.Prescription
     */
    @Override
    public void delete(Patient patient) throws PrescriptionAvailabilityException {
        String sql = "DELETE FROM Patient where id = ?;";
        try (PreparedStatement st = ConnectionUtil.getConnection()
                .prepareStatement(sql)) {
            st.setLong(1, patient.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key no action")) {
                throw new PrescriptionAvailabilityException();
            } else {
                System.out.println(e.getMessage());
            }
        }
    }
}
