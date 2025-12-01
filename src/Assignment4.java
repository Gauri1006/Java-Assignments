import java.io.*;
import java.util.*;

// ===================== STUDENT CLASS =====================
class StudentA4 {
    private Integer rollNo;
    private String name;
    private String email;
    private String course;
    private Double marks;

    public StudentA4(Integer rollNo, String name, String email, String course, Double marks) {
        this.rollNo = rollNo;
        this.name = name;
        this.email = email;
        this.course = course;
        this.marks = marks;
    }

    public Integer getRollNo() { return rollNo; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getCourse() { return course; }
    public Double getMarks() { return marks; }

    @Override
    public String toString() {
        return "Roll No: " + rollNo + "\nName: " + name + "\nEmail: " + email +
                "\nCourse: " + course + "\nMarks: " + marks;
    }

    // Convert to CSV format for file storage
    public String toCSV() {
        return rollNo + "," + name + "," + email + "," + course + "," + marks;
    }

    // Parse CSV line to Student
    public static StudentA4 fromCSV(String line) {
        String[] parts = line.split(",");
        return new StudentA4(Integer.valueOf(parts[0]), parts[1], parts[2], parts[3], Double.valueOf(parts[4]));
    }
}

// ===================== FILE UTIL CLASS =====================
class FileUtilA4 {
    private static final String FILE_NAME = "students.txt";

    public static List<StudentA4> readStudents() {
        List<StudentA4> students = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return students; // Return empty list if file doesn't exist
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                students.add(StudentA4.fromCSV(line));
            }
        } catch (IOException e) {
            System.out.println("❌ Error reading file: " + e.getMessage());
        }
        return students;
    }

    public static void writeStudents(List<StudentA4> students) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (StudentA4 s : students) {
                bw.write(s.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("❌ Error writing file: " + e.getMessage());
        }
    }

    // Example of RandomAccessFile usage
    public static void readRandomAccess() {
        try (RandomAccessFile raf = new RandomAccessFile(FILE_NAME, "r")) {
            System.out.println("Random access read (first 50 chars):");
            byte[] bytes = new byte[50];
            int readBytes = raf.read(bytes);
            System.out.println(new String(bytes, 0, readBytes));
        } catch (IOException e) {
            System.out.println("❌ RandomAccessFile error: " + e.getMessage());
        }
    }
}

// ===================== STUDENT MANAGER =====================
class StudentManagerA4 {
    private List<StudentA4> students = new ArrayList<>();
    private Map<Integer, StudentA4> studentMap = new HashMap<>();
    private Scanner sc = new Scanner(System.in);

    public StudentManagerA4() {
        students = FileUtilA4.readStudents();
        for (StudentA4 s : students) {
            studentMap.put(s.getRollNo(), s);
        }
        System.out.println("Loaded students from file: " + students.size() + " records.");
    }

    public void addStudent() {
        try {
            System.out.print("Enter Roll No: ");
            Integer roll = Integer.valueOf(sc.nextLine().trim());
            if (studentMap.containsKey(roll)) {
                System.out.println("❌ Student with this roll number already exists!");
                return;
            }

            System.out.print("Enter Name: ");
            String name = sc.nextLine().trim();

            System.out.print("Enter Email: ");
            String email = sc.nextLine().trim();

            System.out.print("Enter Course: ");
            String course = sc.nextLine().trim();

            System.out.print("Enter Marks: ");
            Double marks = Double.valueOf(sc.nextLine().trim());

            StudentA4 s = new StudentA4(roll, name, email, course, marks);
            students.add(s);
            studentMap.put(roll, s);
            System.out.println("✅ Student added successfully!");
        } catch (Exception e) {
            System.out.println("❌ Invalid input! " + e.getMessage());
        }
    }

    public void viewAllStudents() {
        if (students.isEmpty()) {
            System.out.println("⚠ No students to display.");
            return;
        }
        System.out.println("===== ALL STUDENTS =====");
        Iterator<StudentA4> it = students.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
            System.out.println("----------------------");
        }
    }

    public void searchByName() {
        System.out.print("Enter Name to search: ");
        String name = sc.nextLine().trim();
        boolean found = false;
        for (StudentA4 s : students) {
            if (s.getName().equalsIgnoreCase(name)) {
                System.out.println(s);
                System.out.println("----------------------");
                found = true;
            }
        }
        if (!found) System.out.println("❌ No student found with name: " + name);
    }

    public void deleteByName() {
        System.out.print("Enter Name to delete: ");
        String name = sc.nextLine().trim();
        Iterator<StudentA4> it = students.iterator();
        boolean found = false;
        while (it.hasNext()) {
            StudentA4 s = it.next();
            if (s.getName().equalsIgnoreCase(name)) {
                it.remove();
                studentMap.remove(s.getRollNo());
                System.out.println("✅ Student deleted: " + s.getName());
                found = true;
            }
        }
        if (!found) System.out.println("❌ No student found with name: " + name);
    }

    public void sortByMarks() {
        students.sort(Comparator.comparingDouble(StudentA4::getMarks).reversed());
        System.out.println("Sorted Student List by Marks:");
        viewAllStudents();
    }

    public void saveToFile() {
        FileUtilA4.writeStudents(students);
        System.out.println("✅ All changes saved to file.");
    }
}

// ===================== MAIN CLASS =====================
public class Assignment4 {
    public static void main(String[] args) {
        StudentManagerA4 manager = new StudentManagerA4();
        Scanner sc = new Scanner(System.in);
        int choice;

        FileUtilA4.readRandomAccess(); // Demo RandomAccessFile

        do {
            System.out.println("\n===== CAPSTONE STUDENT MENU =====");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search by Name");
            System.out.println("4. Delete by Name");
            System.out.println("5. Sort by Marks");
            System.out.println("6. Save and Exit");
            System.out.print("Enter choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine().trim());
                switch (choice) {
                    case 1 -> manager.addStudent();
                    case 2 -> manager.viewAllStudents();
                    case 3 -> manager.searchByName();
                    case 4 -> manager.deleteByName();
                    case 5 -> manager.sortByMarks();
                    case 6 -> {
                        manager.saveToFile();
                        System.out.println("Exiting... Goodbye!");
                        choice = 6;
                    }
                    default -> System.out.println("❌ Invalid choice! Try again.");
                }
            } catch (Exception e) {
                System.out.println("❌ Invalid input! " + e.getMessage());
                choice = 0;
            }

        } while (choice != 6);
    }
}
