package s25.cs151.application;

public class Course {
    private String CourseCode;
    private String CourseName;
    private String SectionNumber;

    public Course(String courseCode, String courseName, String sectionNumber) {
        this.CourseCode = courseCode;
        this.CourseName = courseName;
        this.SectionNumber = sectionNumber;
    }
    public String getCourseCode() {
        return CourseCode;
    }
    public void setCourseCode(String courseCode) {
        CourseCode = courseCode;
    }
    public String getCourseName() {
        return CourseName;
    }
    public void setCourseName(String courseName) {
        CourseName = courseName;
    }
    public String getSectionNumber() {
        return SectionNumber;
    }
    public void setSectionNumber(String sectionNumber) {
        SectionNumber = sectionNumber;
    }
}
