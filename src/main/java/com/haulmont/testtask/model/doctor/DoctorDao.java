package com.haulmont.testtask.model.doctor;

import com.haulmont.testtask.model.Dao;
import com.haulmont.testtask.model.exception.PrescriptionAvailabilityException;
import com.haulmont.testtask.util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDao implements Dao<Doctor> {
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
            doctor.setSpecialization(resultSet.getString("specialization"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return doctor;
    }

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
