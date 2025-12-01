import java.io.*;
import java.util.*;

// ====================== Custom Exception ======================
class StudentNotFoundException extends Exception {
    public StudentNotFoundException(String msg) {
        super(msg);
    }
}

// ====================== Loader Thread =========================
class Loader implements Runnable {
    public void run() {
        try {
            System.out.print("Processing");
            for (int i = 0; i < 5; i++) {
                Thread.sleep(250);
                System.out.print(".");
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println("Thread Interrupted!");
        }
    }
}

// ====================== Abstract Class Person =================
abstract class Person {
    protected String name;
    protected String email;

    public Person(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public abstract void displayInfo();
}

// ====================== Student Class =========================
class Student extends Person {
    int rollNo;
    String course;
    double marks;
    char grade;

    public Student(int rollNo, String name, String email, String course, double marks) {
        super(name, email);
        this.rollNo = rollNo;
        this.course = course;
        this.marks = marks;
        calculateGrade();
    }

    public void calculateGrade() {
        if (marks >= 90) grade = 'A';
        else if (marks >= 75) grade = 'B';
        else if (marks >= 50) grade = 'C';
        else grade = 'D';
    }

    public void update(String name, String email, String course, double marks) {
        this.name = name;
        this.email = email;
        this.course = course;
        this.marks = marks;
        calculateGrade();
    }

    @Override
    public void displayInfo() {
        System.out.println("-------------------------------");
        System.out.println("Roll No : " + rollNo);
        System.out.println("Name    : " + name);
        System.out.println("Email   : " + email);
        System.out.println("Course  : " + course);
        System.out.println("Marks   : " + marks);
        System.out.println("Grade   : " + grade);
        System.out.println("-------------------------------");
    }

    @Override
    public String toString() {
        return rollNo + "," + name + "," + email + "," + course + "," + marks;
    }
}

// ====================== Interface =============================
interface RecordActions {
    void addStudent();
    void deleteStudent();
    void updateStudent();
    void searchStudent() throws StudentNotFoundException;
    void viewAllStudents();
    void sortByMarks();
}

// ====================== File Handling ==========================
class FileUtil {

    public static ArrayList<Student> loadFromFile(String fileName) {
        ArrayList<Student> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            String line;
            while ((line = br.readLine()) != null) {

                String[] d = line.split(",");
                if (d.length == 5) {
                    int roll = Integer.parseInt(d[0]);
                    String name = d[1];
                    String email = d[2];
                    String course = d[3];
                    double marks = Double.parseDouble(d[4]);

                    list.add(new Student(roll, name, email, course, marks));
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("students.txt not found. A new file will be created.");
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return list;
    }

    public static void saveToFile(ArrayList<Student> list, String fileName) {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {

            for (Student s : list) {
                bw.write(s.toString());
                bw.newLine();
            }

            System.out.println("Data saved to students.txt");

        } catch (Exception e) {
            System.out.println("File write error: " + e.getMessage());
        }
    }

    public static void randomRead(String fileName) {
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "r")) {

            System.out.println("RandomAccessFile Length: " + raf.length());
            System.out.print("First 20 characters: ");

            for (int i = 0; i < 20 && i < raf.length(); i++) {
                System.out.print((char) raf.read());
            }

            System.out.println();

        } catch (Exception e) {
            System.out.println("Random access error: " + e.getMessage());
        }
    }
}

// ====================== Student Manager =======================
class StudentManager implements RecordActions {

    ArrayList<Student> students = new ArrayList<>();
    Scanner sc = new Scanner(System.in);

    public StudentManager() {
        students = FileUtil.loadFromFile("students.txt");
    }

    @Override
    public void addStudent() {
        try {
            System.out.print("Enter Roll No: ");
            int roll = sc.nextInt(); sc.nextLine();

            for (Student s : students) {
                if (s.rollNo == roll) {
                    System.out.println("Duplicate roll number!");
                    return;
                }
            }

            System.out.print("Enter Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Email: ");
            String email = sc.nextLine();

            System.out.print("Enter Course: ");
            String course = sc.nextLine();

            System.out.print("Enter Marks: ");
            double marks = sc.nextDouble();

            if (marks < 0 || marks > 100) throw new Exception("Marks must be 0–100");

            Thread t = new Thread(new Loader());
            t.start();
            t.join();

            students.add(new Student(roll, name, email, course, marks));
            System.out.println("Student added!");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            sc.nextLine();
        }
    }

    @Override
    public void deleteStudent() {
        System.out.print("Enter roll no to delete: ");
        int roll = sc.nextInt();

        boolean removed = students.removeIf(s -> s.rollNo == roll);

        if (removed) System.out.println("Student deleted.");
        else System.out.println("Record not found.");
    }

    @Override
    public void updateStudent() {
        System.out.print("Enter roll no to update: ");
        int roll = sc.nextInt(); sc.nextLine();

        for (Student s : students) {
            if (s.rollNo == roll) {

                System.out.print("New Name: ");
                String name = sc.nextLine();

                System.out.print("New Email: ");
                String email = sc.nextLine();

                System.out.print("New Course: ");
                String course = sc.nextLine();

                System.out.print("New Marks: ");
                double marks = sc.nextDouble();

                s.update(name, email, course, marks);
                System.out.println("Updated!");
                return;
            }
        }
        System.out.println("Record not found.");
    }

    @Override
    public void searchStudent() throws StudentNotFoundException {
        System.out.print("Enter roll no to search: ");
        int roll = sc.nextInt();

        for (Student s : students) {
            if (s.rollNo == roll) {
                s.displayInfo();
                return;
            }
        }

        throw new StudentNotFoundException("Student not found!");
    }

    @Override
    public void viewAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No records found.");
            return;
        }

        Iterator<Student> itr = students.iterator();
        while (itr.hasNext()) {
            itr.next().displayInfo();
        }
    }

    @Override
    public void sortByMarks() {
        students.sort((a, b) -> Double.compare(b.marks, a.marks));
        System.out.println("Sorted by marks:");
        viewAllStudents();
    }

    public void saveAndExit() {
        FileUtil.saveToFile(students, "students.txt");
        FileUtil.randomRead("students.txt");
        System.out.println("Exiting...");
        System.exit(0);
    }
}

// ====================== MAIN CLASS ===========================
public class RecordSystem5 {
    public static void main(String[] args) {

        StudentManager manager = new StudentManager();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Capstone Student Menu =====");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search Student");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Sort by Marks");
            System.out.println("7. Save & Exit");
            System.out.print("Enter choice: ");

            int ch = sc.nextInt();

            try {
                switch (ch) {
                    case 1: manager.addStudent(); break;
                    case 2: manager.viewAllStudents(); break;
                    case 3: manager.searchStudent(); break;
                    case 4: manager.updateStudent(); break;
                    case 5: manager.deleteStudent(); break;
                    case 6: manager.sortByMarks(); break;
                    case 7: manager.saveAndExit(); break;
                    default: System.out.println("Invalid choice!");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}