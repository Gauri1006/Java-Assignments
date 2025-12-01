import java.util.*;

// ===================== CUSTOM EXCEPTION =====================
class StudentNotFoundExceptionA3 extends Exception {
    public StudentNotFoundExceptionA3(String message) {
        super(message);
    }
}

// ===================== ABSTRACT PERSON CLASS =====================
abstract class PersonA3 {
    protected String name;
    protected String email;

    public PersonA3(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public abstract void displayInfo();
}

// ===================== STUDENT CLASS =====================
class StudentA3 extends PersonA3 {
    private Integer rollNo;
    private String course;
    private Double marks;
    private char grade;

    public StudentA3(Integer rollNo, String name, String email, String course, Double marks) {
        super(name, email);
        this.rollNo = rollNo;
        this.course = course;
        this.marks = marks;
        calculateGrade();
    }

    private void calculateGrade() {
        if (marks >= 90) grade = 'A';
        else if (marks >= 75) grade = 'B';
        else if (marks >= 60) grade = 'C';
        else if (marks >= 40) grade = 'D';
        else grade = 'F';
    }

    public Integer getRollNo() {
        return rollNo;
    }

    @Override
    public void displayInfo() {
        System.out.println("Roll No: " + rollNo);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Course: " + course);
        System.out.println("Marks: " + marks);
        System.out.println("Grade: " + grade);
    }
}

// ===================== INTERFACE =====================
interface RecordActionsA3 {
    void addStudent();
    void displayStudent(Integer rollNo) throws StudentNotFoundExceptionA3;
}

// ===================== STUDENT MANAGER =====================
class StudentManagerA3 implements RecordActionsA3 {
    private Map<Integer, StudentA3> studentMap = new HashMap<>();
    private Scanner sc = new Scanner(System.in);

    @Override
    public void addStudent() {
        try {
            System.out.print("Enter Roll No (Integer): ");
            Integer rollNo = Integer.valueOf(sc.nextLine().trim());

            System.out.print("Enter Name: ");
            String name = sc.nextLine().trim();
            if (name.isEmpty()) throw new IllegalArgumentException("Name cannot be empty!");

            System.out.print("Enter Email: ");
            String email = sc.nextLine().trim();
            if (email.isEmpty()) throw new IllegalArgumentException("Email cannot be empty!");

            System.out.print("Enter Course: ");
            String course = sc.nextLine().trim();
            if (course.isEmpty()) throw new IllegalArgumentException("Course cannot be empty!");

            System.out.print("Enter Marks: ");
            Double marks = Double.valueOf(sc.nextLine().trim());
            if (marks < 0 || marks > 100)
                throw new IllegalArgumentException("Marks must be between 0 and 100!");

            // Multithreading loading simulation
            LoaderA3 loader = new LoaderA3();
            Thread t = new Thread(loader);
            t.start();
            t.join(); // Wait for thread

            StudentA3 s = new StudentA3(rollNo, name, email, course, marks);
            studentMap.put(rollNo, s);
            s.displayInfo();

        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid numeric input! Please enter valid integers or doubles.");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("❌ Thread interrupted!");
        }
    }

    @Override
    public void displayStudent(Integer rollNo) throws StudentNotFoundExceptionA3 {
        StudentA3 s = studentMap.get(rollNo);
        if (s == null) throw new StudentNotFoundExceptionA3("Student with roll no " + rollNo + " not found!");
        s.displayInfo();
    }
}

// ===================== LOADER CLASS =====================
class LoaderA3 implements Runnable {
    @Override
    public void run() {
        try {
            System.out.print("Loading");
            for (int i = 0; i < 3; i++) {
                Thread.sleep(500);
                System.out.print(".");
            }
            System.out.println();
        } catch (InterruptedException e) {
            System.out.println("Loading interrupted!");
        }
    }
}

// ===================== MAIN CLASS =====================
public class Assignment3 {
    public static void main(String[] args) {
        StudentManagerA3 manager = new StudentManagerA3();
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n===== STUDENT MANAGEMENT MENU =====");
            System.out.println("1. Add Student");
            System.out.println("2. Display Student by Roll No");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine().trim());

                switch (choice) {
                    case 1 -> manager.addStudent();
                    case 2 -> {
                        System.out.print("Enter Roll No to Display: ");
                        Integer roll = Integer.valueOf(sc.nextLine().trim());
                        try {
                            manager.displayStudent(roll);
                        } catch (StudentNotFoundExceptionA3 e) {
                            System.out.println("❌ " + e.getMessage());
                        }
                    }
                    case 3 -> System.out.println("Exiting... Goodbye!");
                    default -> System.out.println("❌ Invalid choice! Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input! Enter a number.");
                choice = 0; // To continue loop
            }

        } while (choice != 3);

        System.out.println("Program execution completed.");
    }
}
