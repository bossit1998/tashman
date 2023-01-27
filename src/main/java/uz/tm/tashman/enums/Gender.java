package uz.tm.tashman.enums;

import uz.tm.tashman.models.HashMapModel;

import java.util.ArrayList;

import static uz.tm.tashman.util.Util.checkLanguage;

public enum Gender {
    MALE(0, "male", "Male", "Мужчина", "Erkak"),
    FEMALE(0, "female", "Female", "Женшина", "Ayol");

    private final long id;
    private final String code;
    private final String englishName;
    private final String russianName;
    private final String uzbekName;

    Gender(int id, String code, String englishName, String russianName, String uzbekName) {
        this.id = id;
        this.code = code;
        this.englishName = englishName;
        this.russianName = russianName;
        this.uzbekName = uzbekName;
    }

    public static Gender getByCode(String value) {
        for (Gender target : values()) {
            if (target.code.equals(value)) {
                return target;
            }
        }
        return null;
    }

    public static Gender getById(long id) {
        for (Gender target : values()) {
            if (target.id == id) {
                return target;
            }
        }
        return null;
    }

    public static String getNameByLanguage(Gender target, Language language) {
        language = checkLanguage(language);

        switch (language) {
            case EN:
                return target.englishName;
            case UZ:
                return target.uzbekName;
            case RU:
            default:
                return target.russianName;
        }
    }

    public static ArrayList<HashMapModel> getAllByLanguage(Language language) {
        language = checkLanguage(language);

        ArrayList<HashMapModel> result = new ArrayList<>();
        switch (language) {
            case EN:
                for (Gender target : values()) {
                    HashMapModel hashMapModel = new HashMapModel();
                    hashMapModel.setLabel(target.englishName);
                    hashMapModel.setValue(target.code);
                    result.add(hashMapModel);
                }
                return result;
            case UZ:
                for (Gender target : values()) {
                    HashMapModel hashMapModel = new HashMapModel();
                    hashMapModel.setLabel(target.uzbekName);
                    hashMapModel.setValue(target.code);
                    result.add(hashMapModel);
                }
                return result;
            case RU:
            default:
                for (Gender target : values()) {
                    HashMapModel hashMapModel = new HashMapModel();
                    hashMapModel.setLabel(target.russianName);
                    hashMapModel.setValue(target.code);
                    result.add(hashMapModel);
                }
                return result;
        }
    }
}