package org.example.repository;

import org.example.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseRepository {
    private Connection connection;

    public CourseRepository() {
        connection = DatabaseConnection.getConnection();
    }
    public void insertCourse(String course, int id) throws SQLException {
        String insertCourseSQL = "INSERT INTO Courses (course_name, teacher_id) VALUES (?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(insertCourseSQL)) {
            statement.setString(1, course);
            statement.setInt(2, id);
            statement.executeUpdate();
            System.out.println("Course inserted successfully.");
        }
    }
    public void deleteCourse(int courseId) throws SQLException {
        String deleteEnrollmentsSQL = "DELETE FROM Enrollments WHERE course_id = ?;";
        String deleteCourseSQL = "DELETE FROM Courses WHERE id = ?;";
        try {
            try (PreparedStatement deleteEnrollmentsStatement = connection.prepareStatement(deleteEnrollmentsSQL)) {
                deleteEnrollmentsStatement.setInt(1, courseId);
                deleteEnrollmentsStatement.executeUpdate();
            }
            try (PreparedStatement deleteCourseStatement = connection.prepareStatement(deleteCourseSQL)) {
                deleteCourseStatement.setInt(1, courseId);
                deleteCourseStatement.executeUpdate();
                System.out.println("Course deleted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getCourseIdByName(String name) throws SQLException {
        String query = "SELECT id FROM Courses WHERE course_name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                return -1;
            }
        }
    }
}
