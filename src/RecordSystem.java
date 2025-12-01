import java.util.*;

// Abstract Class Person
abstract class Person {
    protected String name;
    protected String email;

    public Person(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public abstract void displayInfo();
}

// Student Class - Inherits Person
class Student extends Person {
    private int rollNo;
    private String course;
    private double marks;
    private char grade;

    public Student(int rollNo, String name, String email, String course, double marks) {
        super(name, email);
        this.rollNo = rollNo;
        this.course = course;
        this.marks = marks;
        calculateGrade();
    }

    @Override
    public void displayInfo() {
        System.out.println("---------------------------");
        System.out.println("Roll No: " + rollNo);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Course: " + course);
        System.out.println("Marks: " + marks);
        System.out.println("Grade: " + grade);
        System.out.println("---------------------------");
    }

    // Method Overloading
    public void displayInfo(String note) {
        System.out.println(note);
        displayInfo();
    }

    private void calculateGrade() {
        if (marks >= 90) grade = 'A';
        else if (marks >= 75) grade = 'B';
        else if (marks >= 50) grade = 'C';
        else grade = 'D';
    }

    public int getRollNo() {
        return rollNo;
    }

    public void update(String name, String email, String course, double marks) {
        this.name = name;
        this.email = email;
        this.course = course;
        this.marks = marks;
        calculateGrade();
    }
}

// Interface for CRUD Operations
interface RecordActions {
    void addStudent();
    void deleteStudent();
    void updateStudent();
    void searchStudent();
    void viewAllStudents();
}

// StudentManager Implements RecordActions
class StudentManager implements RecordActions {
    private Map<Integer, Student> students = new HashMap<>();
    private Scanner sc = new Scanner(System.in);

    @Override
    public void addStudent() {
        System.out.print("Enter Roll No: ");
        int roll = sc.nextInt();
        sc.nextLine();

        if (students.containsKey(roll)) {
            System.out.println("Error: Duplicate roll number!");
            return;
        }

        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Email: ");
        String email = sc.nextLine();

        System.out.print("Enter Course: ");
        String course = sc.nextLine();

        System.out.print("Enter Marks: ");
        double marks = sc.nextDouble();

        Student s = new Student(roll, name, email, course, marks);
        students.put(roll, s);
        System.out.println("Student added successfully!");
    }

    @Override
    public void deleteStudent() {
        System.out.print("Enter Roll No to delete: ");
        int roll = sc.nextInt();

        if (students.remove(roll) != null) {
            System.out.println("Student deleted.");
        } else {
            System.out.println("Record not found.");
        }
    }

    @Override
    public void updateStudent() {
        System.out.print("Enter Roll No to update: ");
        int roll = sc.nextInt();
        sc.nextLine();

        if (!students.containsKey(roll)) {
            System.out.println("Record not found.");
            return;
        }

        System.out.print("Enter New Name: ");
        String name = sc.nextLine();

        System.out.print("Enter New Email: ");
        String email = sc.nextLine();

        System.out.print("Enter New Course: ");
        String course = sc.nextLine();

        System.out.print("Enter New Marks: ");
        double marks = sc.nextDouble();

        students.get(roll).update(name, email, course, marks);
        System.out.println("Student updated!");
    }

    @Override
    public void searchStudent() {
        System.out.print("Enter Roll No to search: ");
        int roll = sc.nextInt();

        if (students.containsKey(roll)) {
            students.get(roll).displayInfo();
        } else {
            System.out.println("Record not found.");
        }
    }

    @Override
    public void viewAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No student records found.");
            return;
        }

        for (Student s : students.values()) {
            s.displayInfo();
        }
    }
}

// MAIN CLASS
public class RecordSystem {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        StudentManager manager = new StudentManager();

        while (true) {
            System.out.println("\n===== Student Management Menu =====");
            System.out.println("1. Add Student");
            System.out.println("2. Delete Student");
            System.out.println("3. Update Student");
            System.out.println("4. Search Student");
            System.out.println("5. View All Students");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();

            switch (choice) {
                case 1: manager.addStudent(); break;
                case 2: manager.deleteStudent(); break;
                case 3: manager.updateStudent(); break;
                case 4: manager.searchStudent(); break;
                case 5: manager.viewAllStudents(); break;
                case 6:
                    System.out.println("Exiting... Goodbye!");
                    return;

                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }
}