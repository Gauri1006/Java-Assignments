import java.util.*;

// ===================== ABSTRACT PERSON CLASS =====================
abstract class PersonA2 {
    protected String name;
    protected String email;

    PersonA2() {}

    PersonA2(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public abstract void displayInfo();
}

// ===================== STUDENT CLASS =====================
class StudentA2 extends PersonA2 {
    int rollNo;
    String course;
    double marks;
    char grade;

    StudentA2() {}

    StudentA2(int rollNo, String name, String email, String course, double marks) {
        super(name, email);
        this.rollNo = rollNo;
        this.course = course;
        this.marks = marks;
        this.grade = calculateGrade();
    }

    private char calculateGrade() {
        if (marks >= 85) return 'A';
        else if (marks >= 70) return 'B';
        else if (marks >= 55) return 'C';
        else return 'D';
    }

    @Override
    public void displayInfo() {
        System.out.println("Student Info:");
        System.out.println("Roll No: " + rollNo);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Course: " + course);
        System.out.println("Marks: " + marks);
        System.out.println("Grade: " + grade);
    }

    // Overloading
    public void displayInfo(String researchArea) {
        System.out.println("Student Info:");
        System.out.println("Roll No: " + rollNo);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Course: " + course);
        System.out.println("Research Area: " + researchArea);
    }
}

// ===================== INTERFACE =====================
interface RecordActionsA2 {
    void addStudent();
    void deleteStudent(int rollNo);
    void updateStudent(int rollNo);
    void searchStudent(int rollNo);
    void viewAllStudents();
}

// ===================== STUDENT MANAGER =====================
class StudentManagerA2 implements RecordActionsA2 {
    Map<Integer, StudentA2> studentMap = new HashMap<>();
    Scanner sc = new Scanner(System.in);

    @Override
    public void addStudent() {
        System.out.print("Enter Roll No: ");
        int roll = sc.nextInt();
        sc.nextLine();

        if (studentMap.containsKey(roll)) {
            System.out.println("❌ Student with this roll number already exists!");
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

        StudentA2 s = new StudentA2(roll, name, email, course, marks);
        studentMap.put(roll, s);
        System.out.println("✅ Student Added Successfully!");
    }

    @Override
    public void deleteStudent(int rollNo) {
        if (studentMap.remove(rollNo) != null) {
            System.out.println("✅ Student Deleted Successfully!");
        } else {
            System.out.println("❌ Student Not Found!");
        }
    }

    @Override
    public void updateStudent(int rollNo) {
        StudentA2 s = studentMap.get(rollNo);
        if (s == null) {
            System.out.println("❌ Student Not Found!");
            return;
        }

        sc.nextLine();
        System.out.print("Enter New Course: ");
        s.course = sc.nextLine();

        System.out.print("Enter New Marks: ");
        s.marks = sc.nextDouble();
        s.grade = (s.marks >= 85) ? 'A' : (s.marks >= 70) ? 'B' : (s.marks >= 55) ? 'C' : 'D';

        System.out.println("✅ Student Updated Successfully!");
    }

    @Override
    public void searchStudent(int rollNo) {
        StudentA2 s = studentMap.get(rollNo);
        if (s != null) s.displayInfo();
        else System.out.println("❌ Student Not Found!");
    }

    @Override
    public void viewAllStudents() {
        if (studentMap.isEmpty()) {
            System.out.println("⚠ No Students to Display");
            return;
        }
        System.out.println("===== ALL STUDENTS =====");
        for (StudentA2 s : studentMap.values()) {
            s.displayInfo();
            System.out.println("----------------------");
        }
    }
}

// ===================== MAIN CLASS =====================
public class Assignment2 {
    public static void main(String[] args) {

        StudentManagerA2 manager = new StudentManagerA2();
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n===== STUDENT RECORD MENU =====");
            System.out.println("1. Add Student");
            System.out.println("2. Delete Student");
            System.out.println("3. Update Student");
            System.out.println("4. Search Student");
            System.out.println("5. View All Students");
            System.out.println("6. Demonstrate Overloading");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> manager.addStudent();
                case 2 -> {
                    System.out.print("Enter Roll No to Delete: ");
                    manager.deleteStudent(sc.nextInt());
                }
                case 3 -> {
                    System.out.print("Enter Roll No to Update: ");
                    manager.updateStudent(sc.nextInt());
                }
                case 4 -> {
                    System.out.print("Enter Roll No to Search: ");
                    manager.searchStudent(sc.nextInt());
                }
                case 5 -> manager.viewAllStudents();
                case 6 -> {
                    StudentA2 s = new StudentA2(102, "Riya", "riya@mail.com", "M.Tech", 91);
                    s.displayInfo("AI");
                }
                case 7 -> System.out.println("Exiting... Goodbye!");
                default -> System.out.println("Invalid Choice! Try again.");
            }

        } while (choice != 7);
    }
}

