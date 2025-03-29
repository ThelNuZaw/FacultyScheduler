package s25.cs151.application;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OfficeHourimpDAO implements OfficeHourDAO {
    private final File file = new File("officeHours.csv");

    @Override
    public List<OfficeHour> getAll() {
        List<OfficeHour> list = new ArrayList<>();
        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (parts.length == 3) {
                    list.add(new OfficeHour(parts[0], parts[1], parts[2].replaceAll("^\"|\"$", "")));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        list.sort((o1, o2) -> {
            int y1 = Integer.parseInt(o1.getYear());
            int y2 = Integer.parseInt(o2.getYear());
            if (y1 != y2) return Integer.compare(y2, y1);
            return Integer.compare(trackSemester(o2.getSemester()), trackSemester(o1.getSemester()));
        });

        return list;
    }

    @Override
    public void store(OfficeHour officeHour) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {
            out.println(officeHour.getYear() + "," + officeHour.getSemester() + ",\"" + officeHour.getSelectedDays() + "\"");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(String semester, String year, OfficeHour updated) {
        List<OfficeHour> list = getAll();
        for (int i = 0; i < list.size(); i++) {
            OfficeHour o = list.get(i);
            if (o.getSemester().equals(semester) && o.getYear().equals(year)) {
                list.set(i, updated);
                break;
            }
        }
        overwrite(list);
    }

    @Override
    public void delete(String semester, String year) {
        List<OfficeHour> list = getAll();
        list.removeIf(o -> o.getSemester().equals(semester) && o.getYear().equals(year));
        overwrite(list);
    }

    private void overwrite(List<OfficeHour> list) {
        try (PrintWriter pw = new PrintWriter(file)) {
            for (OfficeHour o : list) {
                pw.println(o.getYear() + "," + o.getSemester() + ",\"" + o.getSelectedDays() + "\"");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int trackSemester(String semester) {
        return switch (semester.trim()) {
            case "Spring" -> 4;
            case "Summer" -> 3;
            case "Fall" -> 2;
            case "Winter" -> 1;
            default -> 0;
        };
    }
}

