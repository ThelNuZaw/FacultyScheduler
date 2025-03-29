package s25.cs151.application;

import java.util.List;

public interface OfficeHourDAO {
    List<OfficeHour> getAll();
    void store(OfficeHour officeHour);
    void update(String semester, String year, OfficeHour updated);
    void delete(String semester, String year);
}
