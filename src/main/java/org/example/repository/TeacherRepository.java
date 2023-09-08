package org.example.repository;

import org.example.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class TeacherRepository {
    private Connection connection;

    public TeacherRepository() {
        connection = DatabaseConnection.getConnection();
    }




    public void insertTeacher(String name, String subject) throws SQLException {

        int count = 0;
        String query = "SELECT COUNT(*) FROM Teachers WHERE name = ? AND school_subject = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, subject);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        }
        if (count>0){
            System.out.println("Teacher with the same name and subject already exists.");
            return;
        }

        String insertTeacherSQL = "INSERT INTO Teachers (name, school_subject) VALUES (?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(insertTeacherSQL)) {
            statement.setString(1, name);
            statement.setString(2, subject);
            statement.executeUpdate();
            System.out.println("Teacher inserted successfully.");
        }
    }

    public int getTeacherIdByName(String name) throws SQLException {
        String query = "SELECT id FROM Teachers WHERE name = ?";
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




    public void deleteTeacher(int teacherId) throws SQLException {
        String deleteCoursesSQL = "DELETE FROM Courses WHERE teacher_id = ?;";
        String deleteTeacherSQL = "DELETE FROM Teachers WHERE id = ?;";
        try {
            try (PreparedStatement deleteCoursesStatement = connection.prepareStatement(deleteCoursesSQL)) {
                deleteCoursesStatement.setInt(1, teacherId);
                deleteCoursesStatement.executeUpdate();
            }
            try (PreparedStatement deleteTeacherStatement = connection.prepareStatement(deleteTeacherSQL)) {
                deleteTeacherStatement.setInt(1, teacherId);
                deleteTeacherStatement.executeUpdate();
                System.out.println("Teacher deleted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
