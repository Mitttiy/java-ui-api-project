package ru.ibs.gasu.client.utils;

import com.google.gwt.i18n.shared.DateTimeFormat;
import ru.ibs.gasu.common.models.GiProjectStatus;
import ru.ibs.gasu.common.soap.generated.gchpdocs.ProjectStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClientUtils {

    public static ProjectStatus getProjectStatus(GiProjectStatus giProjectStatus) {
        ProjectStatus projectStatus = new ProjectStatus();
        projectStatus.setId(giProjectStatus.getId());

        return projectStatus;
    }

    public static List<String> generateComboBoxYearValues() {
        Integer currentYear = Integer.parseInt(getCurrentYear());
        List<String> res = new ArrayList<>();
        for (int i = 2005; i <= currentYear + 50; i++) {
            res.add(String.valueOf(i));
        }
        return res;
    }

    public static List<String> generateComboBoxYearValuesWithSum() {
        Integer currentYear = Integer.parseInt(getCurrentYear());
        List<String> res = new ArrayList<>();
        res.add("Всего");
        for (int i = 2005; i <= currentYear + 50; i++) {
            res.add(String.valueOf(i));
        }
        return res;
    }

    public static List<String> generateComboBoxYearValues(String minYearStr, String maxYearStr) {
        Integer currentYear = Integer.parseInt(getCurrentYear());
        Integer minYear = StringUtils.isEmpty(minYearStr) ? 2005 : Integer.parseInt(minYearStr);
        Integer maxYear = StringUtils.isEmpty(maxYearStr) ?  currentYear + 50 : Integer.parseInt(maxYearStr);
        List<String> res = new ArrayList<>();
        for (int i = minYear; i <= maxYear; i++) {
            res.add(String.valueOf(i));
        }
        return res;
    }

    public static String getCurrentYear() {
        return String.valueOf(new Date().getYear() + 1900);
    }

}
