package com.haulmont.testtask.model.doctor;

import com.haulmont.testtask.model.Dao;
import com.haulmont.testtask.model.exception.PrescriptionAvailabilityException;
import com.haulmont.testtask.util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, реализующий CRUD - методы для сущности {@link Doctor},
 * нужен для общения с БД
 *
 * @see Doctor
 */
public class DoctorDao implements Dao<Doctor> {

    /**
     * Метод для получения доктора из БД по его id. Если отлавливается
     * SQLException, то в консоль выводится его сообщение
     *
     * @param id - уникальный идентификатор доктора
     * @return возвращает доктора, полученного из БД по id
     * @see SQLException
     */
    @Override
    public Doctor get(Long id) {
        String sql = "SELECT * FROM Doctor WHERE id = ?;";
        Doctor doctor = new Doctor();
        try (PreparedStatement statement = ConnectionUtil.getConnection()
                .prepareStatement(sql)) {
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            doctor.setId(resultSet.getLong("id"));
            doctor.setName(resultSet.getString("name"));
            doctor.setSurname(resultSet.getString("surname"));
            doctor.setPatronymic(resultSet.getString("patronymic"));
            doctor.setSpecialization(resultSet
                    .getString("specialization"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return doctor;
    }

    /**
     * Метод для получения всех докторов из БД. Если отлавливается
     * SQLException, то в консоль выводится его сообщение
     *
     * @return возвращает List докторов, полученных из БД
     * @see List
     * @see SQLException
     */
    @Override
    public List<Doctor> getAll() {
        String sql = "SELECT * FROM Doctor;";
        List<Doctor> list = new ArrayList<>();
        try (PreparedStatement statement = ConnectionUtil.getConnection()
                .prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Doctor doctor = new Doctor();
                doctor.setId(resultSet.getLong("id"));
                doctor.setName(resultSet.getString("name"));
                doctor.setSurname(resultSet.getString("surname"));
                doctor.setPatronymic(resultSet.
                        getString("patronymic"));
                doctor.setSpecialization(resultSet.
                        getString("specialization"));
                list.add(doctor);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    /**
     * Метод для сохранения доктора в БД. Если отлавливается
     * SQLException, то в консоль выводится его сообщение
     *
     * @param doctor - доктор для сохранения
     * @see SQLException
     */
    @Override
    public void save(Doctor doctor) {
        String sql = "INSERT INTO Doctor(name, surname, patronymic, " +
                "specialization)"
                + "VALUES(?, ?, ?, ?)";
        try {
            PreparedStatement statement = ConnectionUtil.getConnection()
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, doctor.getName());
            statement.setString(2, doctor.getSurname());
            statement.setString(3, doctor.getPatronymic());
            statement.setString(4, doctor.getSpecialization());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    doctor.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating doctor failed, " +
                            "no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Метод для обновления уже существующего в
     * БД доктора. Если отлавливается SQLException,
     * то в консоль выводится его сообщение
     *
     * @param oldDoctor - доктор, который будет обновлён
     * @param newDoctor - доктор, который нужен
     *                  для обновления oldDoctor
     * @see SQLException
     */
    @Override
    public void update(Doctor oldDoctor, Doctor newDoctor) {
        String sql = "UPDATE Doctor set name = ?, surname = ?, " +
                "patronymic = ?, specialization = ?" +
                "where id = ?";
        try (PreparedStatement statement = ConnectionUtil.getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, newDoctor.getName());
            statement.setString(2, newDoctor.getSurname());
            statement.setString(3, newDoctor.getPatronymic());
            statement.setString(4, newDoctor.getSpecialization());
            statement.setLong(5, oldDoctor.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Метод для удаления доктора из БД. Если отлавливается
     * SQLException и его причиной было удаление доктора, у которого есть
     * {@link com.haulmont.testtask.model.prescription.Prescription},
     * то пробрасывается {@link PrescriptionAvailabilityException}
     *
     * @param doctor - доктор, который будет удалён из БД
     * @throws PrescriptionAvailabilityException - исключение, сигнализирующее
     *                                           о том, что доктор,
     *                                           которого пользователь
     *                                           хочет удалить из бд,
     *                                           имеет рецепты
     * @see SQLException
     * @see PrescriptionAvailabilityException
     * @see com.haulmont.testtask.model.prescription.Prescription
     */
    @Override
    public void delete(Doctor doctor) throws PrescriptionAvailabilityException {
        String sql = "DELETE FROM Doctor where id = ?;";
        try (PreparedStatement st = ConnectionUtil.getConnection()
                .prepareStatement(sql)) {
            st.setLong(1, doctor.getId());
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
