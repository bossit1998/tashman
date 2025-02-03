package uz.tm.tashman.enums;

import lombok.Getter;
import uz.tm.tashman.models.HashMapModel;

import java.util.ArrayList;

@Getter
public enum VolumeUnit {
    ML(0, "ml", "Milliliter", "Миллилитр", "Millilitr"),
    MG(1, "mg", "Milligram", "Миллиграм", "Milligram"),
    L(2, "l", "Liter", "Литр", "Litr"),
    KG(3, "kg", "Kilogram", "Килограм", "Kilogram"),
    CONTAINER(4, "container", "Container", "Контейнер", "Konteyner"),
    PIECE(5, "piece", "Piece", "Штук", "Dona"),
    BOX(6, "box", "Box", "Коробка", "Korobka"),
    ;

    private final long id;
    private final String code;
    private final String englishName;
    private final String russianName;
    private final String uzbekName;

    VolumeUnit(int id, String code, String englishName, String russianName, String uzbekName) {
        this.id = id;
        this.code = code;
        this.englishName = englishName;
        this.russianName = russianName;
        this.uzbekName = uzbekName;
    }

    public static VolumeUnit getByCode(String doseUnit) {
        for (VolumeUnit doseUnits : values()) {
            if (doseUnits.name().equals(doseUnit)) {
                return doseUnits;
            }
        }
        return null;
    }

    public static VolumeUnit getById(long id) {
        for (VolumeUnit doseUnits : values()) {
            if (doseUnits.id == id) {
                return doseUnits;
            }
        }
        return null;
    }


    public static ArrayList<HashMapModel> getAllByLanguage(String language) {
        ArrayList<HashMapModel> volumeUnits = new ArrayList<>();
        switch (language) {
            case "en":
                for (VolumeUnit unit : values()) {
                    HashMapModel volumeUnit = new HashMapModel();
                    volumeUnit.setLabel(unit.englishName);
                    volumeUnit.setValue(unit.code);
                    volumeUnits.add(volumeUnit);
                }
                return volumeUnits;
            case "uz":
                for (VolumeUnit unit : values()) {
                    HashMapModel volumeUnit = new HashMapModel();
                    volumeUnit.setLabel(unit.uzbekName);
                    volumeUnit.setValue(unit.code);
                    volumeUnits.add(volumeUnit);
                }
                return volumeUnits;
            case "ru":
            default:
                for (VolumeUnit unit : values()) {
                    HashMapModel volumeUnit = new HashMapModel();
                    volumeUnit.setLabel(unit.russianName);
                    volumeUnit.setValue(unit.code);
                    volumeUnits.add(volumeUnit);
                }
                return volumeUnits;
        }
    }
}