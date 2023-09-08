package org.example.repository;

import org.example.DatabaseConnection;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EnrollmentRepository {
    private Connection connection;

    public EnrollmentRepository() {
        connection = DatabaseConnection.getConnection();
    }
    public void insertEnrollment(int studentid, int courseid, String enrollmentDate) throws SQLException {
        String insertEnrollmentSQL = "INSERT INTO Enrollments (student_id, course_id, enrollment_date) VALUES (?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(insertEnrollmentSQL)) {
            statement.setInt(1, studentid);
            statement.setInt(2, courseid);
            statement.setString(3, enrollmentDate);
            statement.executeUpdate();
            System.out.println("Enrollment inserted successfully.");
        }
    }
//    public void deleteEnrollment(int enrollmentId) throws SQLException {
//        String deleteEnrollmentSQL = "DELETE FROM Enrollments WHERE id = ?;";
//        try {
//            // Delete the enrollment
//            try (PreparedStatement deleteEnrollmentStatement = connection.prepareStatement(deleteEnrollmentSQL)) {
//                deleteEnrollmentStatement.setInt(1, enrollmentId);
//                deleteEnrollmentStatement.executeUpdate();
//                System.out.println("Enrollment deleted successfully.");
//            }
//        } catch (SQLException e) {
//            // Handle any exceptions here
//            e.printStackTrace();
//        }
//    }

}
