import java.util.*;

// Custom Exception
class StudentNotFoundException extends Exception {
    public StudentNotFoundException(String msg) {
        super(msg);
    }
}

// Loader Thread (Multithreading)
class Loader implements Runnable {
    @Override
    public void run() {
        try {
            System.out.print("Loading");
            for (int i = 0; i < 5; i++) {
                Thread.sleep(300);
                System.out.print(".");
            }
            System.out.println();
        } catch (InterruptedException e) {
            System.out.println("Loading interrupted!");
        }
    }
}

// Student Class
class Student {
    private Integer rollNo;
    private String name;
    private String email;
    private String course;
    private Double marks;
    private char grade;

    public Student(Integer rollNo, String name, String email, String course, Double marks) {
        this.rollNo = rollNo;
        this.name = name;
        this.email = email;
        this.course = course;
        this.marks = marks;
        calculateGrade();
    }

    private void calculateGrade() {
        if (marks >= 90) grade = 'A';
        else if (marks >= 75) grade = 'B';
        else if (marks >= 50) grade = 'C';
        else grade = 'D';
    }

    public Integer getRollNo() {
        return rollNo;
    }

    public void display() {
        System.out.println("------------------------------");
        System.out.println("Roll No: " + rollNo);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Course: " + course);
        System.out.println("Marks: " + marks);
        System.out.println("Grade: " + grade);
        System.out.println("------------------------------");
    }
}

// Interface
interface RecordActions {
    void addStudent();
    void searchStudent() throws StudentNotFoundException;
    void viewAllStudents();
}

// Student Manager implementing interface
class StudentManager implements RecordActions {

    private Map<Integer, Student> students = new HashMap<>();
    private Scanner sc = new Scanner(System.in);

    // Helper: Validate Marks
    private Double validateMarks(Double marks) throws Exception {
        if (marks < 0 || marks > 100)
            throw new Exception("Marks must be between 0 and 100!");
        return marks;
    }

    @Override
    public void addStudent() {
        try {
            System.out.print("Enter Roll No (Integer): ");
            Integer roll = Integer.valueOf(sc.nextInt());
            sc.nextLine();

            if (students.containsKey(roll)) {
                System.out.println("Duplicate Roll No! Cannot add.");
                return;
            }

            System.out.print("Enter Name: ");
            String name = sc.nextLine();
            if (name.isEmpty()) throw new Exception("Name cannot be empty!");

            System.out.print("Enter Email: ");
            String email = sc.nextLine();
            if (email.isEmpty()) throw new Exception("Email cannot be empty!");

            System.out.print("Enter Course: ");
            String course = sc.nextLine();
            if (course.isEmpty()) throw new Exception("Course cannot be empty!");

            System.out.print("Enter Marks (Double): ");
            Double marks = Double.valueOf(sc.nextDouble());

            marks = validateMarks(marks); // exception check

            // Multithreading Loader
            Thread t = new Thread(new Loader());
            t.start();
            t.join();

            Student s = new Student(roll, name, email, course, marks);
            students.put(roll, s);

            System.out.println("Student added successfully!");

        } catch (InputMismatchException e) {
            System.out.println("Invalid Input Type! Please enter correct values.");
            sc.nextLine();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            System.out.println("Operation completed.\n");
        }
    }

    @Override
    public void searchStudent() throws StudentNotFoundException {
        System.out.print("Enter Roll No to search: ");
        Integer roll = Integer.valueOf(sc.nextInt());

        if (!students.containsKey(roll)) {
            throw new StudentNotFoundException("Student with Roll No " + roll + " not found!");
        }

        students.get(roll).display();
    }

    @Override
    public void viewAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No records found.");
            return;
        }
        for (Student s : students.values()) {
            s.display();
        }
    }
}

// MAIN CLASS — renamed as requested
public class RecordSystem3 {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        StudentManager manager = new StudentManager();

        while (true) {
            System.out.println("===== Student Management Menu =====");
            System.out.println("1. Add Student");
            System.out.println("2. Search Student");
            System.out.println("3. View All Students");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    manager.addStudent();
                    break;

                case 2:
                    try {
                        manager.searchStudent();
                    } catch (StudentNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 3:
                    manager.viewAllStudents();
                    break;

                case 4:
                    System.out.println("Program execution completed.");
                    return;

                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }
}