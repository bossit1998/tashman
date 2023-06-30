package uz.tm.tashman.enums;

import lombok.Getter;
import uz.tm.tashman.models.HashMapModel;

import java.util.ArrayList;

import static uz.tm.tashman.entity.Product.getString;
import static uz.tm.tashman.util.Util.checkLanguage;

@Getter
public enum TimeUnits {
    DAY(0, "day", "Day", "День", "Kun"),
    WEEK(1, "week", "Week", "Неделья", "Hafta"),
    MONTH(2, "month", "Month", "Месяц", "Oy"),
    YEAR(3, "year", "Year", "Год", "Yil");

    private final long id;
    private final String code;
    private final String englishName;
    private final String russianName;
    private final String uzbekName;

    TimeUnits(int id, String code, String englishName, String russianName, String uzbekName) {
        this.id = id;
        this.code = code;
        this.englishName = englishName;
        this.russianName = russianName;
        this.uzbekName = uzbekName;
    }

    public static TimeUnits getByCode(String doseUnit) {
        for (TimeUnits timeUnits : values()) {
            if (timeUnits.name().equals(doseUnit)) {
                return timeUnits;
            }
        }
        return null;
    }

    public static TimeUnits getById(long id) {
        for (TimeUnits timeUnits : values()) {
            if (timeUnits.id == id) {
                return timeUnits;
            }
        }
        return null;
    }

    public String getNameByLanguage(Language language) {
        return getString(language, englishName, uzbekName, russianName);
    }

    public static ArrayList<HashMapModel> getAllByLanguage(Language language) {
        language = checkLanguage(language);

        ArrayList<HashMapModel> timeUnits = new ArrayList<>();
        switch (language) {
            case EN:
                for (TimeUnits unit : values()) {
                    HashMapModel timeUnit = new HashMapModel();
                    timeUnit.setLabel(unit.englishName);
                    timeUnit.setValue(unit.code);
                    timeUnits.add(timeUnit);
                }
                return timeUnits;
            case UZ:
                for (TimeUnits unit : values()) {
                    HashMapModel timeUnit = new HashMapModel();
                    timeUnit.setLabel(unit.uzbekName);
                    timeUnit.setValue(unit.code);
                    timeUnits.add(timeUnit);
                }
                return timeUnits;
            case RU:
            default:
                for (TimeUnits unit : values()) {
                    HashMapModel timeUnit = new HashMapModel();
                    timeUnit.setLabel(unit.russianName);
                    timeUnit.setValue(unit.code);
                    timeUnits.add(timeUnit);
                }
                return timeUnits;
        }
    }
}