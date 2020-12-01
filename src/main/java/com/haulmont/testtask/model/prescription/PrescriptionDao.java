package com.haulmont.testtask.model.prescription;

import com.haulmont.testtask.model.Dao;
import com.haulmont.testtask.model.doctor.Doctor;
import com.haulmont.testtask.model.doctor.DoctorDao;
import com.haulmont.testtask.model.patient.PatientDao;
import com.haulmont.testtask.util.ConnectionUtil;
import com.haulmont.testtask.view.prescription.PrescriptionPriority;

import java.sql.*;
import java.util.*;

public class PrescriptionDao implements Dao<Prescription> {

    @Override
    public Prescription get(Long id) {
        String sql = "SELECT * FROM prescription WHERE id = ?;";
        Prescription prescription = new Prescription();
        try (PreparedStatement statement = ConnectionUtil.getConnection()
                .prepareStatement(sql)) {

            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            PatientDao patientDao = new PatientDao();
            DoctorDao doctorDao = new DoctorDao();

            prescription.setId(resultSet.getLong("id"));
            prescription.setDescription(resultSet.
                    getString("description"));
            prescription.setPatient(patientDao.get(resultSet.
                    getLong("patient_id")));
            prescription.setDoctor(doctorDao.get(resultSet.
                    getLong("doctor_id")));
            prescription.setDateCreated(resultSet.
                    getDate("date_created"));
            prescription.setExpirationDate(resultSet.
                    getDate("expiration_date"));
            prescription.setPriority(PrescriptionPriority.valueOf(resultSet.
                    getString("priority")));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return prescription;
    }

    @Override
    public List<Prescription> getAll() {
        String sql = "SELECT * FROM prescription;";
        List<Prescription> list = new ArrayList<>();
        try (PreparedStatement statement = ConnectionUtil.getConnection()
                .prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            PatientDao patientDao = new PatientDao();
            DoctorDao doctorDao = new DoctorDao();
            while (resultSet.next()) {
                Prescription prescription = new Prescription();
                prescription.setId(resultSet
                        .getLong("id"));
                prescription.setDescription(resultSet
                        .getString("description"));
                prescription.setPatient(patientDao.get(resultSet.
                        getLong("patient_id")));
                prescription.setDoctor(doctorDao.get(resultSet.
                        getLong("doctor_id")));
                prescription.setDateCreated(resultSet.
                        getDate("date_created"));
                prescription.setExpirationDate(resultSet.
                        getDate("expiration_date"));
                prescription.setPriority(PrescriptionPriority.valueOf(resultSet
                        .getString("priority")));
                list.add(prescription);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    @Override
    public void save(Prescription prescription) {
        String sql = "INSERT INTO prescription(description, patient_id, " +
                "doctor_id, date_created, expiration_date, priority)" +
                "VALUES(?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = ConnectionUtil.getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, prescription.getDescription());
            statement.setLong(2, prescription.getPatient().getId());
            statement.setLong(3, prescription.getDoctor().getId());
            statement.setDate(4, prescription.getDateCreated());
            statement.setDate(5, prescription.getExpirationDate());
            statement.setString(6, prescription
                    .getPriority().toString());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    prescription.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating prescription failed, " +
                            "no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(Prescription oldPrescription,
                       Prescription updatedPrescription) {
        String sql = "UPDATE prescription set description = ?, patient_id = ?, " +
                "doctor_id = ?, date_created = ?, expiration_date = ?, " +
                "priority = ? where id = ?";
        try (PreparedStatement statement = ConnectionUtil.getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, updatedPrescription
                    .getDescription());
            statement.setLong(2, updatedPrescription
                    .getPatient().getId());
            statement.setLong(3, updatedPrescription
                    .getDoctor().getId());
            statement.setDate(4, updatedPrescription
                    .getDateCreated());
            statement.setDate(5, updatedPrescription
                    .getExpirationDate());
            statement.setString(6, updatedPrescription
                    .getPriority().toString());
            statement.setLong(7, oldPrescription.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Prescription prescription) {
        String sql = "DELETE FROM prescription where id = ?;";
        try (PreparedStatement st = ConnectionUtil.getConnection()
                .prepareStatement(sql)) {
            st.setLong(1, prescription.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Map<Doctor, Long> getQuantityOfPrescriptions() {
        String sql = "SELECT doctor_id, COUNT(DISTINCT id) as prescriptions_num " +
                "FROM prescription GROUP BY doctor_id";
        Map<Doctor, Long> doctorQuantityMap = new LinkedHashMap<>();
        try (PreparedStatement statement = ConnectionUtil.getConnection()
                .prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                DoctorDao doctorDao = new DoctorDao();
                Doctor doctor = doctorDao.get(resultSet
                        .getLong("doctor_id"));
                Long numOfPrescriptions = resultSet
                        .getLong("prescriptions_num");
                doctorQuantityMap.put(doctor, numOfPrescriptions);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return doctorQuantityMap;
    }
}
