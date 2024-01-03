package uz.tm.tashman.enums;

import lombok.Getter;
import uz.tm.tashman.models.HashMapModel;

import java.util.ArrayList;

import static uz.tm.tashman.util.Util.checkLanguage;

@Getter
public enum TimeUnits {
    DAY(0, "day", "Day", "День", "Kun"),
    WEEK(1, "week", "Week", "Неделья", "Hafta"),
    MONTH(2, "month", "Month", "Месяц", "Oy"),
    YEAR(3, "year", "Year", "Год", "Yil");

    private final long id;
    private final String code;
    private final String nameEn;
    private final String nameRu;
    private final String nameUz;

    TimeUnits(int id, String code, String nameEn, String nameRu, String nameUz) {
        this.id = id;
        this.code = code;
        this.nameEn = nameEn;
        this.nameRu = nameRu;
        this.nameUz = nameUz;
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

    public String getName(Language language) {
        switch (language) {
            case EN:
                return nameEn;
            case UZ:
                return nameUz;
            case RU:
            default:
                return nameRu;
        }
    }

    public static ArrayList<HashMapModel> getAllByLanguage(Language language) {
        language = checkLanguage(language);

        ArrayList<HashMapModel> timeUnits = new ArrayList<>();
        switch (language) {
            case EN:
                for (TimeUnits unit : values()) {
                    HashMapModel timeUnit = new HashMapModel();
                    timeUnit.setLabel(unit.nameEn);
                    timeUnit.setValue(unit.code);
                    timeUnits.add(timeUnit);
                }
                return timeUnits;
            case UZ:
                for (TimeUnits unit : values()) {
                    HashMapModel timeUnit = new HashMapModel();
                    timeUnit.setLabel(unit.nameUz);
                    timeUnit.setValue(unit.code);
                    timeUnits.add(timeUnit);
                }
                return timeUnits;
            case RU:
            default:
                for (TimeUnits unit : values()) {
                    HashMapModel timeUnit = new HashMapModel();
                    timeUnit.setLabel(unit.nameRu);
                    timeUnit.setValue(unit.code);
                    timeUnits.add(timeUnit);
                }
                return timeUnits;
        }
    }
}