package org.example.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class StudentRepositoryTest {

    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockStatement;
    @Mock
    private ResultSet mockResultSet;

    private StudentRepository studentRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        studentRepository = new StudentRepository(mockConnection);
    }

    @Test
    public void testInsertStudent() throws SQLException {
        // Defina os dados do aluno de teste
        String testName = "Test Student";
        String testDateOfBirth = "2000-01-01";
        String testAddress = "123 Main Street";

        // Simule o comportamento do PreparedStatement
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1); // Indica que a inserção foi bem-sucedida

        // Simule o comportamento do ResultSet para retornar um resultado quando executeQuery() for chamado
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);

        // Simule o comportamento do ResultSet para o método next()
        when(mockResultSet.next()).thenReturn(true); // Indica que há uma linha de resultado
        when(mockResultSet.getInt("id")).thenReturn(1); // ID do aluno inserido

        // Execute o método de inserção
        try {
            studentRepository.insertStudent(testName, testDateOfBirth, testAddress);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Verifique se o PreparedStatement foi chamado com os parâmetros corretos
        verify(mockStatement).setString(1, testName);
        verify(mockStatement).setString(2, testDateOfBirth);
        verify(mockStatement).setString(3, testAddress);
        verify(mockStatement).executeUpdate();

        // Verifique se o aluno foi inserido com sucesso (ID não é -1)
        int insertedStudentId = studentRepository.getStudentIdByName(testName);
        assertNotEquals(-1, insertedStudentId);

        // Verifique se o método de obtenção de ID foi chamado
        verify(mockStatement).executeQuery();
        verify(mockResultSet).next();
        verify(mockResultSet).getInt("id");
    }

//    @Test
//    public void testQueryStudentsInCourse() throws SQLException {
//        String courseName = "Math"; // Nome do curso para o qual você deseja testar
//
//        // Consulta SQL esperada com base no courseName
//        String expectedQuery = "SELECT Students.Name " +
//                "FROM Students " +
//                "INNER JOIN Enrollments ON Students.ID = Enrollments.Student_ID " +
//                "INNER JOIN Courses ON Enrollments.Course_ID = Courses.ID " +
//                "WHERE Courses.Course_Name = ?";
//
//        // Simule o comportamento do PreparedStatement
//        when(mockConnection.prepareStatement(expectedQuery)).thenReturn(mockStatement);
//        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
//
//        // Simule o comportamento do ResultSet para retornar resultados esperados
//        when(mockResultSet.next()).thenReturn(true, true, false); // Simula dois alunos na resposta
//        when(mockResultSet.getString("Name")).thenReturn("Alice", "Bob"); // Nomes dos alunos
//
//        // Execute o método de consulta
//        studentRepository.queryStudentsInCourse(courseName);
//
//        // Verifique se a consulta foi preparada corretamente com o nome do curso
//        verify(mockStatement).setString(1, courseName);
//
//        // Verifique se o método next() foi chamado duas vezes (para dois alunos)
//        verify(mockResultSet, times(2)).next();
//
//        // Verifique se os nomes dos alunos foram recuperados corretamente
//        //verify(mockResultSet).getString("Name");
//        verify(mockResultSet, times(2)).getString("Name");
//
//
//        // Você pode adicionar verificações adicionais aqui com base nos resultados esperados
//    }
}

