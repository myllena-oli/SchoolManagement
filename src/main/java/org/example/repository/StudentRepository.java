package org.example.repository;

import org.example.DatabaseConnection;
import org.example.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository {
    private Connection connection;

    public StudentRepository() {
        connection = DatabaseConnection.getConnection();
    }
    public StudentRepository(Connection connection) {
        this.connection = connection;
    }

    public void insertStudent(String name, String dateOfBirth, String address) throws SQLException {
        String insertStudentSQL = "INSERT INTO students (name, dateofbirth, address) VALUES (?, ?, ?);";
        PreparedStatement statement = connection.prepareStatement(insertStudentSQL);
        statement.setString(1, name);
        statement.setString(2, dateOfBirth);
        statement.setString(3, address);
        statement.executeUpdate();
        System.out.println("Student inserted successfully.");
    }
    public void queryStudentsInCourse(String courseName) throws SQLException {
        String query = "SELECT Students.Name " +
                "FROM Students " +
                "INNER JOIN Enrollments ON Students.ID = Enrollments.Student_ID " +
                "INNER JOIN Courses ON Enrollments.Course_ID = Courses.ID " +
                "WHERE Courses.Course_Name = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, courseName);
            ResultSet resultSet = statement.executeQuery();

            System.out.println("Students enrolled in course " + courseName + ":");
            while (resultSet.next()) {
                String studentName = resultSet.getString("Name");
                System.out.println(studentName);
            }
        }
    }
    public void queryCoursesByTeacher(String teacherName) throws SQLException {
        String query = "SELECT Courses.Course_Name " +
                "FROM Courses " +
                "INNER JOIN Teachers ON Courses.Teacher_ID = Teachers.ID " +
                "WHERE Teachers.Name = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, teacherName);
            ResultSet resultSet = statement.executeQuery();

            System.out.println("Courses taught by " + teacherName + ":");
            while (resultSet.next()) {
                String courseName = resultSet.getString("Course_Name");
                System.out.println(courseName);
            }
        }
    }
    public void queryStudentsNotEnrolled() throws SQLException {
        String query = "SELECT Students.Name " +
                "FROM Students " +
                "LEFT JOIN Enrollments ON Students.ID = Enrollments.Student_ID " +
                "WHERE Enrollments.Student_ID IS NULL";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            System.out.println("Students not enrolled in any course:");
            while (resultSet.next()) {
                String studentName = resultSet.getString("Name");
                System.out.println(studentName);
            }
        }
    }
    public void queryCoursesWithoutStudents() throws SQLException {
        String query = "SELECT Courses.Course_Name " +
                "FROM Courses " +
                "LEFT JOIN Enrollments ON Courses.ID = Enrollments.Course_ID " +
                "WHERE Enrollments.Course_ID IS NULL";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            System.out.println("Courses without any enrolled students:");
            while (resultSet.next()) {
                String courseName = resultSet.getString("Course_Name");
                System.out.println(courseName);
            }
        }
    }

    public void queryStudentsEnrolledInMultipleCourses() throws SQLException {
        String query = "SELECT Students.Name " +
                "FROM Students " +
                "INNER JOIN Enrollments ON Students.ID = Enrollments.Student_ID " +
                "GROUP BY Students.ID, Students.Name " +
                "HAVING COUNT(Enrollments.Course_ID) > 1";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            System.out.println("Students enrolled in more than one course:");
            while (resultSet.next()) {
                String studentName = resultSet.getString("Name");
                System.out.println(studentName);
            }
        }
    }

    public void deleteStudent(int studentId) throws SQLException {
        String deleteEnrollmentsSQL = "DELETE FROM Enrollments WHERE student_id = ?;";
        String deleteStudentSQL = "DELETE FROM Students WHERE id = ?;";
        try {
            try (PreparedStatement deleteEnrollmentsStatement = connection.prepareStatement(deleteEnrollmentsSQL)) {
                deleteEnrollmentsStatement.setInt(1, studentId);
                deleteEnrollmentsStatement.executeUpdate();
            }
            try (PreparedStatement deleteStudentStatement = connection.prepareStatement(deleteStudentSQL)) {
                deleteStudentStatement.setInt(1, studentId);
                deleteStudentStatement.executeUpdate();
                System.out.println("Student deleted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getStudentIdByName(String name) throws SQLException {
        String query = "SELECT id FROM Students WHERE name = ?";
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
