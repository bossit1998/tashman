package uz.tm.tashman.enums;

import lombok.Getter;
import uz.tm.tashman.models.HashMapModel;

import java.util.ArrayList;

import static uz.tm.tashman.util.Util.checkLanguage;

@Getter
public enum Unit {
    ML(0, "ml", "Milliliter", "Миллилитр", "Millilitr"),
    MG(1, "mg", "Milligram", "Миллиграм", "Milligram"),
    L(2, "l", "Liter", "Литр", "Litr"),
    KG(3, "kg", "Kilogram", "Килограм", "Kilogram"),
    CONTAINER(4, "container", "Container", "Контейнер", "Konteyner"),
    PIECE(5, "piece", "Piece", "Штук", "Dona"),
    BOX(6, "box", "Box", "Коробка", "Korobka"),
    TONN(7, "tonn", "Tonn", "Тонна", "Tonna"),
    ;

    private final long id;
    private final String code;
    private final String nameEn;
    private final String nameRu;
    private final String nameUz;

    Unit(int id, String code, String nameEn, String nameRu, String nameUz) {
        this.id = id;
        this.code = code;
        this.nameEn = nameEn;
        this.nameRu = nameRu;
        this.nameUz = nameUz;
    }

    public static Unit getByCode(String searchVolumeUnitCode) {
        for (Unit unit : values()) {
            if (unit.name().equals(searchVolumeUnitCode)) {
                return unit;
            }
        }
        return null;
    }

    public static Unit getById(long id) {
        for (Unit doseUnits : values()) {
            if (doseUnits.id == id) {
                return doseUnits;
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

        ArrayList<HashMapModel> volumeUnits = new ArrayList<>();
        switch (language) {
            case EN:
                for (Unit unit : values()) {
                    HashMapModel volumeUnit = new HashMapModel();
                    volumeUnit.setLabel(unit.nameEn);
                    volumeUnit.setValue(unit.code);
                    volumeUnits.add(volumeUnit);
                }
                return volumeUnits;
            case UZ:
                for (Unit unit : values()) {
                    HashMapModel volumeUnit = new HashMapModel();
                    volumeUnit.setLabel(unit.nameUz);
                    volumeUnit.setValue(unit.code);
                    volumeUnits.add(volumeUnit);
                }
                return volumeUnits;
            case RU:
            default:
                for (Unit unit : values()) {
                    HashMapModel volumeUnit = new HashMapModel();
                    volumeUnit.setLabel(unit.nameRu);
                    volumeUnit.setValue(unit.code);
                    volumeUnits.add(volumeUnit);
                }
                return volumeUnits;
        }
    }
}