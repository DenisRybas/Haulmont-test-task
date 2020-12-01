package com.haulmont.testtask.model.patient;

import com.haulmont.testtask.model.Dao;
import com.haulmont.testtask.model.exception.PrescriptionAvailabilityException;
import com.haulmont.testtask.util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDao implements Dao<Patient> {
    @Override
    public Patient get(Long id) {
        String sql = "SELECT * FROM Patient WHERE id = ?;";
        Patient patient = new Patient();
        try (PreparedStatement statement = ConnectionUtil.getConnection().prepareStatement(sql)) {

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

    @Override
    public List<Patient> getAll() {
        String sql = "SELECT * FROM Patient;";
        List<Patient> list = new ArrayList<>();
        try (PreparedStatement statement = ConnectionUtil.getConnection().prepareStatement(sql)) {
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

    @Override
    public void save(Patient patient) {
        String sql = "INSERT INTO Patient(name, surname, patronymic, phone_number)"
                + "VALUES(?, ?, ?, ?)";
        try (PreparedStatement statement = ConnectionUtil.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, patient.getName());
            statement.setString(2, patient.getSurname());
            statement.setString(3, patient.getPatronymic());
            statement.setString(4, patient.getPhoneNumber());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    patient.setId(generatedKeys.getLong(1));
                }
                else {
                    throw new SQLException("Creating patient failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(Patient oldPatient, Patient newPatient) {
        String sql = "UPDATE Patient set name = ?, surname = ?, patronymic = ?, phone_number = ?"
                + "where id = ?";
        try (PreparedStatement statement = ConnectionUtil.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
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

    @Override
    public void delete(Patient patient) throws PrescriptionAvailabilityException {
        String sql = "DELETE FROM Patient where id = ?;";
        try (PreparedStatement st = ConnectionUtil.getConnection().prepareStatement(sql)) {
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
