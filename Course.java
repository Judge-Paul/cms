public class Course {

    private final String courseCode;
    private final String courseTitle;
    private final int creditUnits;

    public Course(String courseCode, String courseTitle, int creditUnits) {
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.creditUnits = creditUnits;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public int getCreditUnits() {
        return creditUnits;
    }

    @Override
    public String toString() {
        return courseCode + " - " + courseTitle + " (" + creditUnits + " units)";
    }
}
