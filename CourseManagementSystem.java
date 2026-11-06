import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class CourseManagementSystem {

    private static final String DEFAULT_FILE_NAME = "courses.txt";
    private static final String SEPARATOR = "==================================================";
    private static final String LINE = "--------------------------------------------------";

    private final Scanner scanner = new Scanner(System.in);
    private final ArrayList<Course> courses = new ArrayList<>();

    public void start() {
        System.out.println(SEPARATOR);
        System.out.println("       STUDENT COURSE MANAGEMENT SYSTEM");
        System.out.println(SEPARATOR);
        runMenu();
        scanner.close();
    }

    private void runMenu() {
        displayMenu();

        String input = readLine();
        if (input == null) {
            System.out.println("\nNo more input available. Exiting the application.");
            return;
        }

        int choice;
        try {
            choice = Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            choice = -1;
        }

        switch (choice) {
            case 1:
                addCourse();
                break;
            case 2:
                viewAllCourses();
                break;
            case 3:
                searchCourseByCode();
                break;
            case 4:
                computeTotalUnits();
                break;
            case 5:
                saveCoursesToFile();
                break;
            case 6:
                loadCoursesFromFile();
                break;
            case 0:
                System.out.println("\nThank you for using the Student Course Management System. Goodbye!");
                return;
            default:
                System.out.println("\nError: invalid selection. Please enter a number from 0 to 6");
        }

        // The menu calls itself again, and option 0 above is the base case that stops it.
        runMenu();
    }

    private void displayMenu() {
        System.out.println();
        System.out.println("1. Add Course");
        System.out.println("2. View All Courses");
        System.out.println("3. Search Course by Code");
        System.out.println("4. Compute Total Units");
        System.out.println("5. Save Courses to File");
        System.out.println("6. Load Courses from File");
        System.out.println("0. Exit");
        System.out.print("Select an option: ");
    }

    private void addCourse() {
        System.out.println("\n--- Add Course ---");

        String code = promptRequired("Enter course code: ");
        if (code == null) {
            return;
        }
        if (!isValidCourseCode(code)) {
            System.out.println("Error: a course code may only contain letters, digits, spaces and hyphens.");
            return;
        }
        if (findCourse(code) != null) {
            System.out.println("Error: a course with the code \"" + code + "\" already exists.");
            return;
        }

        String title = promptRequired("Enter course title: ");
        if (title == null) {
            return;
        }
        if (title.contains("|")) {
            System.out.println("Error: the course title cannot contain the \"|\" character.");
            return;
        }

        Integer units = promptCreditUnits();
        if (units == null) {
            return;
        }

        Course course = new Course(code, title, units);
        courses.add(course);
        System.out.println("Success: course added.");
        System.out.println("  " + course);
    }

    private void viewAllCourses() {
        System.out.println("\n--- All Registered Courses ---");

        if (courses.isEmpty()) {
            System.out.println("No courses have been registered yet.");
            return;
        }

        System.out.println(LINE);
        System.out.printf("%-12s %-34s %s%n", "CODE", "TITLE", "UNITS");
        System.out.println(LINE);
        for (Course course : courses) {
            System.out.printf("%-12s %-34s %d%n",
                    course.getCourseCode(), course.getCourseTitle(), course.getCreditUnits());
        }
        System.out.println(LINE);
        System.out.println("Total courses displayed: " + courses.size());
    }

    private void searchCourseByCode() {
        System.out.println("\n--- Search Course by Code ---");

        if (courses.isEmpty()) {
            System.out.println("No courses have been registered yet, so there is nothing to search.");
            return;
        }

        System.out.print("Enter course code to search: ");
        String input = readLine();
        if (input == null) {
            return;
        }

        String code = input.trim();
        if (code.isEmpty()) {
            System.out.println("Error: the search code cannot be empty.");
            return;
        }

        Course found = findCourse(code);
        if (found == null) {
            System.out.println("No course was found with the code \"" + code + "\".");
        } else {
            System.out.println("Course found:");
            System.out.println("  " + found);
        }
    }

    private void computeTotalUnits() {
        System.out.println("\n--- Compute Total Units ---");

        if (courses.isEmpty()) {
            System.out.println("No courses have been registered yet. Total courses: 0, total units: 0.");
            return;
        }

        int totalUnits = 0;
        for (Course course : courses) {
            totalUnits = totalUnits + course.getCreditUnits();
        }

        System.out.println("Total courses: " + courses.size());
        System.out.println("Total credit units: " + totalUnits);
    }

    private void saveCoursesToFile() {
        System.out.println("\n--- Save Courses to File ---");

        if (courses.isEmpty()) {
            System.out.println("There are no courses to save.");
            return;
        }

        String fileName = promptFileName("Enter file name or path to save to");
        if (fileName == null) {
            return;
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (Course course : courses) {
                writer.println(course.getCourseCode() + "|" + course.getCourseTitle() + "|"
                        + course.getCreditUnits());
            }
        } catch (IOException e) {
            System.out.println("Error: the courses could not be saved to " + fileName + ".");
            System.out.println("Reason: " + e.getMessage());
            return;
        }

        System.out.println("Success: " + courses.size() + " course(s) saved to " + fileName + ".");
    }

    private void loadCoursesFromFile() {
        System.out.println("\n--- Load Courses from File ---");

        String fileName = promptFileName("Enter file name or path to load from");
        if (fileName == null) {
            return;
        }

        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("No saved file named " + fileName + " was found.");
            return;
        }
        if (file.length() == 0) {
            System.out.println("The file " + fileName + " is empty. Nothing was loaded.");
            return;
        }

        int loadedCount = 0;
        int skippedCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\|", 3);
                if (parts.length != 3) {
                    skippedCount++;
                    continue;
                }

                String code = parts[0].trim();
                String title = parts[1].trim();
                String unitsText = parts[2].trim();

                int units;
                try {
                    units = Integer.parseInt(unitsText);
                } catch (NumberFormatException e) {
                    skippedCount++;
                    continue;
                }

                if (code.isEmpty() || title.isEmpty() || units <= 0) {
                    skippedCount++;
                    continue;
                }

                if (findCourse(code) != null) {
                    skippedCount++;
                    continue;
                }

                courses.add(new Course(code, title, units));
                loadedCount++;
            }
        } catch (IOException e) {
            System.out.println("Error: the file " + fileName + " could not be read.");
            System.out.println("Reason: " + e.getMessage());
            return;
        }

        System.out.println("Successfully loaded " + loadedCount + " course(s) from " + fileName + ".");
        if (skippedCount > 0) {
            System.out.println("Skipped " + skippedCount
                    + " record(s) because they were malformed or duplicated an existing course.");
        }
    }

    private Course findCourse(String code) {
        for (Course course : courses) {
            if (course.getCourseCode().equalsIgnoreCase(code)) {
                return course;
            }
        }
        return null;
    }

    private boolean isValidCourseCode(String code) {
        for (int i = 0; i < code.length(); i++) {
            char character = code.charAt(i);
            boolean allowed = Character.isLetterOrDigit(character)
                    || character == ' ' || character == '-';
            if (!allowed) {
                return false;
            }
        }
        return true;
    }

    private String promptFileName(String label) {
        System.out.println(label + " (press Enter to use " + DEFAULT_FILE_NAME + "):");
        System.out.print("> ");
        String input = readLine();
        if (input == null) {
            return null;
        }

        String fileName = input.trim();
        if (fileName.isEmpty()) {
            return DEFAULT_FILE_NAME;
        }
        return fileName;
    }

    private String promptRequired(String label) {
        while (true) {
            System.out.print(label);
            String input = readLine();
            if (input == null) {
                return null;
            }

            String value = input.trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Error: this field cannot be empty. Please try again.");
        }
    }

    private Integer promptCreditUnits() {
        while (true) {
            System.out.print("Enter credit units: ");
            String input = readLine();
            if (input == null) {
                return null;
            }

            String text = input.trim();
            if (text.isEmpty()) {
                System.out.println("Error: credit units cannot be empty. Please try again.");
                continue;
            }

            try {
                int units = Integer.parseInt(text);
                if (units <= 0) {
                    System.out.println("Error: credit units must be a positive whole number.");
                    continue;
                }
                return units;
            } catch (NumberFormatException e) {
                System.out.println("Error: credit units must be a whole number. Please try again.");
            }
        }
    }

    private String readLine() {
        try {
            return scanner.nextLine();
        } catch (NoSuchElementException e) {
            return null;
        }
    }
}
