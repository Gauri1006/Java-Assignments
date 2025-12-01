import java.io.*;
import java.util.*;

// ===================== CUSTOM EXCEPTION =====================
class StudentNotFoundException extends Exception {
    public StudentNotFoundException(String message) {
        super(message);
    }
}

// ===================== ABSTRACT CLASS =====================
abstract class Person {
    protected String name;
    protected String email;

    public Person(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public abstract void displayInfo();
}

// ===================== STUDENT CLASS =====================
class Student5 extends Person {
    private int rollNo;
    private String course;
    private double marks;
    private char grade;

    public Student5(int rollNo, String name, String email, String course, double marks) {
        super(name, email);
        this.rollNo = rollNo;
        this.course = course;
        this.marks = marks;
        calculateGrade();
    }

    // ===== GETTERS =====
    public int getRollNo() { return rollNo; }
    public String getCourse() { return course; }
    public double getMarks() { return marks; }
    public char getGrade() { return grade; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    // ===== SETTERS =====
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setCourse(String course) { this.course = course; }
    public void setMarks(double marks) { this.marks = marks; calculateGrade(); }

    public void calculateGrade() {
        if (marks >= 85) grade = 'A';
        else if (marks >= 70) grade = 'B';
        else if (marks >= 50) grade = 'C';
        else grade = 'D';
    }

    @Override
    public void displayInfo() {
        System.out.println("Roll No: " + rollNo);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Course: " + course);
        System.out.println("Marks: " + marks);
        System.out.println("Grade: " + grade);
        System.out.println("----------------------");
    }

    public String toCSV() {
        return rollNo + "," + name + "," + email + "," + course + "," + marks;
    }

    public static Student5 fromCSV(String line) {
        String[] parts = line.split(",");
        return new Student5(
            Integer.parseInt(parts[0]),
            parts[1],
            parts[2],
            parts[3],
            Double.parseDouble(parts[4])
        );
    }
}

// ===================== INTERFACE =====================
interface RecordActions {
    void addStudent();
    void deleteStudent() throws StudentNotFoundException;
    void updateStudent();
    void searchStudent() throws StudentNotFoundException;
    void viewAllStudents();
}

// ===================== LOADER CLASS =====================
class Loader implements Runnable {
    @Override
    public void run() {
        System.out.print("Loading");
        try {
            for (int i = 0; i < 5; i++) {
                Thread.sleep(300);
                System.out.print(".");
            }
            System.out.println();
        } catch (InterruptedException e) {
            System.out.println("❌ Loading interrupted.");
        }
    }
}

// ===================== STUDENT MANAGER =====================
class StudentManager5 implements RecordActions {
    private List<Student5> students = new ArrayList<>();
    private Map<Integer, Student5> studentMap = new HashMap<>();
    private Scanner sc = new Scanner(System.in);
    private final String FILE_NAME = "students.txt";

    public StudentManager5() {
        loadFromFile();
    }

    private void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                Student5 s = Student5.fromCSV(line);
                students.add(s);
                studentMap.put(s.getRollNo(), s);
            }
        } catch (IOException e) {
            System.out.println("❌ Error loading file: " + e.getMessage());
        }
    }

    private void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Student5 s : students) {
                bw.write(s.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("❌ Error saving file: " + e.getMessage());
        }
    }

    @Override
    public void addStudent() {
        try {
            System.out.print("Enter Roll No: ");
            int roll = Integer.parseInt(sc.nextLine());
            if (studentMap.containsKey(roll)) {
                System.out.println("❌ Roll No already exists!");
                return;
            }

            System.out.print("Enter Name: ");
            String name = sc.nextLine().trim();

            System.out.print("Enter Email: ");
            String email = sc.nextLine().trim();

            System.out.print("Enter Course: ");
            String course = sc.nextLine().trim();

            System.out.print("Enter Marks: ");
            double marks = Double.parseDouble(sc.nextLine());
            if (marks < 0 || marks > 100) {
                System.out.println("❌ Marks must be between 0 and 100.");
                return;
            }

            Thread loader = new Thread(new Loader());
            loader.start();
            loader.join();

            Student5 s = new Student5(roll, name, email, course, marks);
            students.add(s);
            studentMap.put(roll, s);
            System.out.println("✅ Student added successfully!");
        } catch (Exception e) {
            System.out.println("❌ Invalid input: " + e.getMessage());
        }
    }

    @Override
    public void deleteStudent() throws StudentNotFoundException {
        System.out.print("Enter name to delete: ");
        String name = sc.nextLine().trim();
        boolean found = false;
        Iterator<Student5> it = students.iterator();
        while (it.hasNext()) {
            Student5 s = it.next();
            if (s.getName().equalsIgnoreCase(name)) {
                it.remove();
                studentMap.remove(s.getRollNo());
                found = true;
                System.out.println("✅ Student record deleted.");
            }
        }
        if (!found) throw new StudentNotFoundException("Student not found.");
    }

    @Override
    public void updateStudent() {
        System.out.print("Enter Roll No to update: ");
        int roll = Integer.parseInt(sc.nextLine());
        if (!studentMap.containsKey(roll)) {
            System.out.println("❌ Student not found.");
            return;
        }
        Student5 s = studentMap.get(roll);

        System.out.print("Enter new Name (" + s.getName() + "): ");
        String name = sc.nextLine().trim();
        if (!name.isEmpty()) s.setName(name);

        System.out.print("Enter new Email (" + s.getEmail() + "): ");
        String email = sc.nextLine().trim();
        if (!email.isEmpty()) s.setEmail(email);

        System.out.print("Enter new Course (" + s.getCourse() + "): ");
        String course = sc.nextLine().trim();
        if (!course.isEmpty()) s.setCourse(course);

        System.out.print("Enter new Marks (" + s.getMarks() + "): ");
        String marksStr = sc.nextLine().trim();
        if (!marksStr.isEmpty()) {
            double marks = Double.parseDouble(marksStr);
            s.setMarks(marks);
        }

        System.out.println("✅ Student updated successfully!");
    }

    @Override
    public void searchStudent() throws StudentNotFoundException {
        System.out.print("Enter name to search: ");
        String name = sc.nextLine().trim();
        boolean found = false;
        for (Student5 s : students) {
            if (s.getName().equalsIgnoreCase(name)) {
                System.out.println("Student Info:");
                s.displayInfo();
                found = true;
            }
        }
        if (!found) throw new StudentNotFoundException("Student not found.");
    }

    @Override
    public void viewAllStudents() {
        if (students.isEmpty()) {
            System.out.println("⚠ No students to display.");
            return;
        }
        System.out.println("===== ALL STUDENTS =====");
        Iterator<Student5> it = students.iterator();
        while (it.hasNext()) it.next().displayInfo();
    }

    public void sortByMarks() {
        students.sort(Comparator.comparingDouble(Student5::getMarks).reversed());
        System.out.println("Sorted Student List by Marks:");
        viewAllStudents();
    }

    public void saveAndExit() {
        Thread loader = new Thread(new Loader());
        try {
            loader.start();
            loader.join();
            saveToFile();
            System.out.println("✅ Saved and exiting.");
        } catch (InterruptedException e) {
            System.out.println("❌ Error during saving: " + e.getMessage());
        }
    }
}

// ===================== MAIN CLASS =====================
public class Assignment5 {
    public static void main(String[] args) {
        StudentManager5 manager = new StudentManager5();
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n===== CAPSTONE STUDENT MENU =====");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search by Name");
            System.out.println("4. Delete by Name");
            System.out.println("5. Sort by Marks");
            System.out.println("6. Update Student");
            System.out.println("7. Save and Exit");
            System.out.print("Enter choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine().trim());
                switch (choice) {
                    case 1 -> manager.addStudent();
                    case 2 -> manager.viewAllStudents();
                    case 3 -> {
                        try { manager.searchStudent(); }
                        catch (StudentNotFoundException e) { System.out.println("❌ " + e.getMessage()); }
                    }
                    case 4 -> {
                        try { manager.deleteStudent(); }
                        catch (StudentNotFoundException e) { System.out.println("❌ " + e.getMessage()); }
                    }
                    case 5 -> manager.sortByMarks();
                    case 6 -> manager.updateStudent();
                    case 7 -> { manager.saveAndExit(); choice = 7; }
                    default -> System.out.println("❌ Invalid choice!");
                }
            } catch (Exception e) {
                System.out.println("❌ Invalid input: " + e.getMessage());
                choice = 0;
            }

        } while (choice != 7);
    }
}
