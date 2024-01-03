package uz.tm.tashman.enums;

import uz.tm.tashman.models.HashMapModel;

import java.util.ArrayList;

import static uz.tm.tashman.util.Util.checkLanguage;

public enum Gender {
    MALE(0, "male", "Male", "Мужчина", "Erkak"),
    FEMALE(0, "female", "Female", "Женшина", "Ayol");

    private final long id;
    private final String code;
    private final String nameEn;
    private final String nameRu;
    private final String nameUz;

    Gender(int id, String code, String nameEn, String nameRu, String nameUz) {
        this.id = id;
        this.code = code;
        this.nameEn = nameEn;
        this.nameRu = nameRu;
        this.nameUz = nameUz;
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

    public static String getName(Gender target, Language language) {
        switch (language) {
            case EN:
                return target.nameEn;
            case UZ:
                return target.nameUz;
            case RU:
            default:
                return target.nameRu;
        }
    }

    public static ArrayList<HashMapModel> getAllByLanguage(Language language) {
        language = checkLanguage(language);

        ArrayList<HashMapModel> result = new ArrayList<>();
        switch (language) {
            case EN:
                for (Gender target : values()) {
                    HashMapModel hashMapModel = new HashMapModel();
                    hashMapModel.setLabel(target.nameEn);
                    hashMapModel.setValue(target.code);
                    result.add(hashMapModel);
                }
                return result;
            case UZ:
                for (Gender target : values()) {
                    HashMapModel hashMapModel = new HashMapModel();
                    hashMapModel.setLabel(target.nameUz);
                    hashMapModel.setValue(target.code);
                    result.add(hashMapModel);
                }
                return result;
            case RU:
            default:
                for (Gender target : values()) {
                    HashMapModel hashMapModel = new HashMapModel();
                    hashMapModel.setLabel(target.nameRu);
                    hashMapModel.setValue(target.code);
                    result.add(hashMapModel);
                }
                return result;
        }
    }
}