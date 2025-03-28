package s25.cs151.application;

public class OfficeHour {
    private String year;
    private String semester;
    private String selectedDays;

    public OfficeHour() {}

    public OfficeHour(String year, String semester, String selectedDays) {
        this.year = year;
        this.semester = semester;
        this.selectedDays = selectedDays;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getSelectedDays() {
        return selectedDays;
    }

    public void setSelectedDays(String selectedDays) {
        this.selectedDays = selectedDays;
    }
}
