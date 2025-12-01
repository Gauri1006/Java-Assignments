import java.io.*;
import java.util.*;

// Student Class
class Student {
    int rollNo;
    String name;
    String email;
    String course;
    double marks;

    public Student(int rollNo, String name, String email, String course, double marks) {
        this.rollNo = rollNo;
        this.name = name;
        this.email = email;
        this.course = course;
        this.marks = marks;
    }

    @Override
    public String toString() {
        return rollNo + "," + name + "," + email + "," + course + "," + marks;
    }
}

// File Handling Utilities
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
            System.out.println("students.txt not found — creating new file on save.");
        } catch (Exception e) {
            System.out.println("File Read Error: " + e.getMessage());
        }

        return list;
    }

    public static void saveToFile(ArrayList<Student> list, String fileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {

            for (Student s : list) {
                bw.write(s.toString());
                bw.newLine();
            }

            System.out.println("Students saved successfully!");

        } catch (Exception e) {
            System.out.println("File Write Error: " + e.getMessage());
        }
    }

    public static void readRandomly(String fileName) {
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "r")) {

            System.out.println("RandomAccessFile length: " + raf.length());
            System.out.print("First 20 characters: ");

            for (int i = 0; i < 20 && i < raf.length(); i++) {
                System.out.print((char) raf.read());
            }
            System.out.println();

        } catch (Exception e) {
            System.out.println("RandomAccessFile Error: " + e.getMessage());
        }
    }
}

// StudentManager
class StudentManager {

    ArrayList<Student> students = new ArrayList<>();
    Scanner sc = new Scanner(System.in);

    public StudentManager() {
        students = FileUtil.loadFromFile("students.txt");
    }

    public void addStudent() {
        try {
            System.out.print("Enter Roll No: ");
            int roll = sc.nextInt(); sc.nextLine();

            System.out.print("Enter Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Email: ");
            String email = sc.nextLine();

            System.out.print("Enter Course: ");
            String course = sc.nextLine();

            System.out.print("Enter Marks: ");
            double marks = sc.nextDouble();

            students.add(new Student(roll, name, email, course, marks));
            System.out.println("Student Added!");

        } catch(Exception e) {
            System.out.println("Invalid Input! Try again.");
            sc.nextLine();
        }
    }

    public void viewAllStudents() {
        Iterator<Student> itr = students.iterator();
        while (itr.hasNext()) {
            Student s = itr.next();
            System.out.println("--------------------");
            System.out.println("Roll No : " + s.rollNo);
            System.out.println("Name    : " + s.name);
            System.out.println("Email   : " + s.email);
            System.out.println("Course  : " + s.course);
            System.out.println("Marks   : " + s.marks);
        }
    }

    public void searchByName() {
        sc.nextLine();
        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        boolean found = false;

        for (Student s : students) {
            if (s.name.equalsIgnoreCase(name)) {
                System.out.println("Found → Roll No: " + s.rollNo + ", Marks: " + s.marks);
                found = true;
            }
        }

        if (!found) System.out.println("No student found!");
    }

    public void deleteByName() {
        sc.nextLine();
        System.out.print("Enter Name to Delete: ");
        String name = sc.nextLine();

        students.removeIf(s -> s.name.equalsIgnoreCase(name));
        System.out.println("Deleted if existed.");
    }

    public void sortByMarks() {
        students.sort((a, b) -> Double.compare(b.marks, a.marks));
        System.out.println("Sorted by Marks:");
        viewAllStudents();
    }

    public void saveAndExit() {
        FileUtil.saveToFile(students, "students.txt");
        FileUtil.readRandomly("students.txt");
        System.out.println("Exiting Program...");
        System.exit(0);
    }
}

// MAIN CLASS (Renamed)
public class RecordSystem4 {
    public static void main(String[] args) {

        StudentManager manager = new StudentManager();
        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("\n===== Student File Menu =====");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search by Name");
            System.out.println("4. Delete by Name");
            System.out.println("5. Sort by Marks");
            System.out.println("6. Save & Exit");
            System.out.print("Enter choice: ");

            int ch = sc.nextInt();

            switch (ch) {
                case 1: manager.addStudent(); break;
                case 2: manager.viewAllStudents(); break;
                case 3: manager.searchByName(); break;
                case 4: manager.deleteByName(); break;
                case 5: manager.sortByMarks(); break;
                case 6: manager.saveAndExit(); break;
                default: System.out.println("Invalid Choice!");
            }
        }
    }
}
