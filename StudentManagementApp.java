import java.sql.*;
import java.util.*;

// Model Class
class Student {
    private int studentID;
    private String name;
    private String department;
    private int marks;

    public Student(int studentID, String name, String department, int marks) {
        this.studentID = studentID;
        this.name = name;
        this.department = department;
        this.marks = marks;
    }

    public int getStudentID() { return studentID; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public int getMarks() { return marks; }

    public void setName(String name) { this.name = name; }
    public void setDepartment(String department) { this.department = department; }
    public void setMarks(int marks) { this.marks = marks; }
}

// Controller Class
class StudentController {
    private Connection con;

    public StudentController() throws SQLException {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/yourdbname", "root", "password");
    }

    public void addStudent(Student s) throws SQLException {
        PreparedStatement pst = con.prepareStatement("INSERT INTO Student VALUES (?, ?, ?, ?)");
        pst.setInt(1, s.getStudentID());
        pst.setString(2, s.getName());
        pst.setString(3, s.getDepartment());
        pst.setInt(4, s.getMarks());
        pst.executeUpdate();
    }

    public void showAllStudents() throws SQLException {
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM Student");
        System.out.println("\n-- Student Records --");
        while (rs.next()) {
            System.out.println("ID: " + rs.getInt(1) + " | Name: " + rs.getString(2)
                    + " | Dept: " + rs.getString(3) + " | Marks: " + rs.getInt(4));
        }
    }

    public void updateStudent(Student s) throws SQLException {
        PreparedStatement pst = con.prepareStatement(
                "UPDATE Student SET Name=?, Department=?, Marks=? WHERE StudentID=?");
        pst.setString(1, s.getName());
        pst.setString(2, s.getDepartment());
        pst.setInt(3, s.getMarks());
        pst.setInt(4, s.getStudentID());
        pst.executeUpdate();
    }

    public void deleteStudent(int id) throws SQLException {
        PreparedStatement pst = con.prepareStatement("DELETE FROM Student WHERE StudentID=?");
        pst.setInt(1, id);
        pst.executeUpdate();
    }
}

// View & Main Application
public class StudentManagementApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            StudentController controller = new StudentController();
            while (true) {
                System.out.println("\n====== Student Management Menu ======");
                System.out.println("1. Add Student");
                System.out.println("2. View All Students");
                System.out.println("3. Update Student");
                System.out.println("4. Delete Student");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int ch = sc.nextInt();
                sc.nextLine();

                switch (ch) {
                    case 1:
                        System.out.print("Enter Student ID: ");
                        int id = sc.nextInt(); sc.nextLine();
                        System.out.print("Enter Name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter Department: ");
                        String dept = sc.nextLine();
                        System.out.print("Enter Marks: ");
                        int marks = sc.nextInt();
                        Student s1 = new Student(id, name, dept, marks);
                        controller.addStudent(s1);
                        System.out.println("Student added successfully!");
                        break;

                    case 2:
                        controller.showAllStudents();
                        break;

                    case 3:
                        System.out.print("Enter Student ID to update: ");
                        id = sc.nextInt(); sc.nextLine();
                        System.out.print("Enter New Name: ");
                        name = sc.nextLine();
                        System.out.print("Enter New Department: ");
                        dept = sc.nextLine();
                        System.out.print("Enter New Marks: ");
                        marks = sc.nextInt();
                        Student s2 = new Student(id, name, dept, marks);
                        controller.updateStudent(s2);
                        System.out.println("Student updated successfully!");
                        break;

                    case 4:
                        System.out.print("Enter Student ID to delete: ");
                        id = sc.nextInt();
                        controller.deleteStudent(id);
                        System.out.println("Student deleted successfully!");
                        break;

                    case 5:
                        System.out.println("Exiting...");
                        return;

                    default:
                        System.out.println("Invalid choice! Try again.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }
}
