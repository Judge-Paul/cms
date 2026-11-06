# Student Course Management System

A simple, CLI application written in Java for recording and managing the courses a student is taking in a semester. It was built for the COS 201 assignment and uses only the standard Java library.

## Features

1. Add a course (course code, title and credit units).
2. View all registered courses in a table.
3. Search for a course by code (case-insensitive).
4. Compute the total number of courses and total credit units.
5. Save all courses to a file of your choice.
6. Load courses back from a file of your choice.
7. Exit the application.

The application validates input, prevents duplicate course codes, handles file errors, and returns to the main menu after every operation.

## Project Files

| File                          | Purpose                                                                     |
| ----------------------------- | --------------------------------------------------------------------------- |
| `Main.java`                   | Application entry point. Creates and starts the system.                     |
| `Course.java`                 | Course model with a constructor, getters, encapsulation and `toString()`.   |
| `CourseManagementSystem.java` | Menu, recursive menu loop, course operations, validation and file handling. |
| `courses.txt`                 | Example file containing sample course records.                              |

## Requirements

- JDK 8 or newer (tested with JDK 17).
- A terminal or command prompt.

## How to Compile

Open a terminal in the project folder and run:

```bash
javac *.java
```

This produces `Main.class`, `Course.class` and `CourseManagementSystem.class`.

## How to Run

From the same folder:

```bash
java Main
```

On some systems you may need:

```bash
java -cp . Main
```

Make sure you run the program from the folder where you want the default `courses.txt` file to be created, or type a full path when saving.

## How to Use

When the program starts, a menu is displayed. Type the number of an option and press Enter.

- **Add Course** asks for a code, title and credit units. Blank fields are rejected and the credit units must be a positive whole number. Course codes are compared without regard to upper/lower case, so `COS 201` and `cos 201` are treated as the same course.
- **View All Courses** prints a formatted table. If the list is empty, a message is shown instead.
- **Search Course by Code** trims spaces and ignores case, so `  cos 201  ` finds `COS 201`.
- **Compute Total Units** adds up the credit units and also reports how many courses are registered.
- **Save Courses to File** asks for a file name or path, then writes one course per line to that file. Pressing Enter without typing anything uses `courses.txt`. The folder you name must already exist.
- **Load Courses from File** asks for a file name or path, reads that file, skips malformed records and duplicates, and reports how many records were loaded and skipped. Pressing Enter without typing anything uses `courses.txt`.
- **Exit** ends the program.

## File Format

Each line in a saved course file contains three fields separated by a pipe character:

```text
CODE|TITLE|UNITS
```

Example:

```text
COS 201|Introduction to Programming|3
COS 202|Object Oriented Programming|3
MTH 101|Elementary Mathematics I|4
ENG 101|Communication Skills|2
```

Course titles entered in the application may not contain the `|` character, which keeps the format reliable when loading.

## Testing Checklist

The following scenarios were tested successfully:

- Adding several courses.
- Rejecting a duplicate code, including one written in different case.
- Rejecting non-numeric, zero and negative credit units, then accepting a valid value.
- Viewing and computing totals with an empty list.
- Searching for an existing code and for a missing code.
- Saving courses to a custom file name and to a custom path, and confirming the files are created.
- Restarting the program and loading the saved courses from those custom paths.
- Loading when the file is missing, empty, or contains malformed and duplicate records.
- Loading with the default `courses.txt` name by pressing Enter.
- Entering an invalid menu selection and returning to the menu.
- Exiting cleanly with option 0.
