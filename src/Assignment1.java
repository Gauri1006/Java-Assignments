import java.util.ArrayList;
import java.util.Scanner;

class Student {
    private int rollNo;
    private String name;
    private String course;
    private double marks;
    private char grade;

    // Default Constructor
    Student() {
        this.rollNo = 0;
        this.name = "";
        this.course = "";
        this.marks = 0.0;
        this.grade = 'F';
    }

    // Parameterized Constructor
    Student(int rollNo, String name, String course, double marks) {
        this.rollNo = rollNo;
        this.name = name;
        this.course = course;
        this.marks = marks;
        calculateGrade();
    }

    // Method to take input from user
    public void inputDetails() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Roll No: ");
        rollNo = sc.nextInt();
        sc.nextLine(); // consume newline

        System.out.print("Enter Name: ");
        name = sc.nextLine();

        System.out.print("Enter Course: ");
        course = sc.nextLine();

        // Validation for marks
        while (true) {
            System.out.print("Enter Marks (0-100): ");
            marks = sc.nextDouble();
            if (marks >= 0 && marks <= 100)
                break;
            System.out.println("Invalid marks! Please enter between 0 and 100.");
        }

        calculateGrade();
    }

    // Display Details
    public void displayDetails() {
        System.out.println("Roll No: " + rollNo);
        System.out.println("Name: " + name);
        System.out.println("Course: " + course);
        System.out.println("Marks: " + marks);
        System.out.println("Grade: " + grade);
        System.out.println("---------------------------");
    }

    // Calculate Grade
    public void calculateGrade() {
        if (marks >= 90)
            grade = 'A';
        else if (marks >= 75)
            grade = 'B';
        else if (marks >= 60)
            grade = 'C';
        else if (marks >= 40)
            grade = 'D';
        else
            grade = 'F';
    }
}

public class Assignment1 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Student> students = new ArrayList<>();

        int choice;

        while (true) {
            System.out.println("===== Student Record Menu =====");
            System.out.println("1. Add Student");
            System.out.println("2. Display All Students");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    Student s = new Student();
                    s.inputDetails();
                    students.add(s);
                    System.out.println("Student added successfully!\n");
                    break;

                case 2:
                    if (students.isEmpty()) {
                        System.out.println("No student records found.\n");
                    } else {
                        System.out.println("\n===== Student Records =====");
                        for (Student stu : students) {
                            stu.displayDetails();
                        }
                    }
                    break;

                case 3:
                    System.out.println("Exiting the application. Goodbye!");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice! Try again.\n");
            }
        }
    }
}
