package org.example;

import org.example.repository.CourseRepository;
import org.example.repository.EnrollmentRepository;
import org.example.repository.StudentRepository;
import org.example.repository.TeacherRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class SchoolManagementApp {
    private Scanner scanner;
    private StudentRepository studentRepository;
    private TeacherRepository teacherRepository;
    private CourseRepository courseRepository;
    private EnrollmentRepository enrollmentRepository;

    public SchoolManagementApp() {
        scanner = new Scanner(System.in);
        studentRepository = new StudentRepository();
        teacherRepository = new TeacherRepository();
        courseRepository = new CourseRepository();
        enrollmentRepository = new EnrollmentRepository();
    }

    public void run() {
        while (true) {
            displayMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    addTeacher();
                    break;
                case 3:
                    addCourse();
                    break;
                case 4:
                    enrollStudent();
                    break;
                case 5:
                    deleteStudent();
                    break;
                case 6:
                    deleteTeacher();
                    break;
                case 7:
                    deleteCourse();
                    break;
                case 8:
                    queryStudentsInACourse();
                    break;
                case 9:
                    queryCoursesByTeacher();
                    break;
                case 10:
                    queryStudentsNotEnrolled();
                    break;
                case 11:
                    queryCoursesWithoutStudents();
                    break;
                case 12:
                    queryStudentsEnrolledInMultipleCourses();
                    break;
                case 13:
                    System.out.println("Exiting...");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addStudent() {
        System.out.print("Enter student name: ");
        String studentName = scanner.nextLine();
        System.out.print("Enter student date of birth: ");
        String studentDOB = scanner.nextLine();
        System.out.print("Enter student address: ");
        String studentAddress = scanner.nextLine();
        try {
            studentRepository.insertStudent(studentName, studentDOB, studentAddress);
            System.out.println("Student added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error adding student.");
        }
    }

    private void addTeacher() {
        System.out.print("Enter teacher name: ");
        String teacherName = scanner.nextLine();
        System.out.print("Enter teacher school subject: ");
        String teacherSubject = scanner.nextLine();
        try {
            teacherRepository.insertTeacher(teacherName, teacherSubject);
            System.out.println("Teacher added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error adding teacher.");
        }
    }

    private void addCourse() {
        System.out.print("Enter course name: ");
        String courseName = scanner.nextLine();
        System.out.print("Enter teacher name: ");
        String teacherName = scanner.nextLine();

        try {
            int teacherId = teacherRepository.getTeacherIdByName(teacherName);
            if (teacherId != -1) {
                courseRepository.insertCourse(courseName, teacherId);
                System.out.println("Course added successfully.");
            } else {
                System.out.println("Teacher with the specified ID does not exist. Course cannot be added.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error adding course.");
        }
    }

    private void enrollStudent() {
        LocalDate today = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String enrollmentDate = today.format(formatter);

        System.out.print("Enter student name: ");
        String studentName = scanner.nextLine();

        System.out.print("Enter course name: ");
        String courseName = scanner.nextLine();

        try {
            int studentId = studentRepository.getStudentIdByName(studentName);
            int courseId = courseRepository.getCourseIdByName(courseName);

            if (studentId != -1 && courseId != -1) {

                enrollmentRepository.insertEnrollment(studentId, courseId, enrollmentDate);
                System.out.println("Student enrolled in the course successfully.");
            } else {
                if (studentId == -1) {
                    System.out.println("Student with the specified name does not exist.");
                }
                if (courseId == -1) {
                    System.out.println("Course with the specified name does not exist.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error enrolling the student in the course.");
        }

    }
    private void deleteStudent(){
        System.out.print("Enter the student ID to delete: ");
        int studentIdToDelete = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character
        try {
            studentRepository.deleteStudent(studentIdToDelete);
            System.out.println("Student deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting student.");
        }
    }
    private void deleteTeacher(){
        System.out.print("Enter teacher ID to delete: ");
        int teacherIdToDelete = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        try {
            teacherRepository.deleteTeacher(teacherIdToDelete);
            System.out.println("Teacher deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting teacher.");
        }
    }
    private void deleteCourse(){
        System.out.print("Enter course ID to delete: ");
        int courseIdToDelete = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        try {
            courseRepository.deleteCourse(courseIdToDelete);
            System.out.println("Course deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting course.");
        }
    }

    private void queryStudentsInACourse(){
        System.out.print("Enter course name to query students: ");
        String courseNameToQuery = scanner.nextLine();

        try {
            studentRepository.queryStudentsInCourse(courseNameToQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error querying students in the course.");
        }
    }

    private void queryCoursesByTeacher(){
        System.out.print("Enter teacher name to query courses: ");
        String teacherNameToQuery = scanner.nextLine();

        try {
            studentRepository.queryCoursesByTeacher(teacherNameToQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error querying courses by teacher.");
        }
    }
    private void queryStudentsNotEnrolled(){
        try {
            studentRepository.queryStudentsNotEnrolled();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error querying students not enrolled.");
        }
    }
    private void queryCoursesWithoutStudents(){
        try {
            studentRepository.queryCoursesWithoutStudents();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error querying courses without students.");
        }
    }
    private void queryStudentsEnrolledInMultipleCourses(){
        try {
            studentRepository.queryStudentsEnrolledInMultipleCourses();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error querying students enrolled in multiple courses.");
        }
    }

    private void displayMenu() {
        System.out.println("\nSchool Management System");
        System.out.println("1. Add Student");
        System.out.println("2. Add Teacher");
        System.out.println("3. Add Course");
        System.out.println("4. Enroll Student");
        System.out.println("5. Delete Student");
        System.out.println("6. Delete Teacher");
        System.out.println("7. Delete Course");
        System.out.println("8. Query Students in a Course");
        System.out.println("9. Query Courses by Teacher");
        System.out.println("10. Query Students Not Enrolled");
        System.out.println("11. Query Courses Without Students");
        System.out.println("12. Query Students Enrolled in Multiple Courses");
        System.out.println("13. Exit\n");
    }

    private int getUserChoice() {
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }
}