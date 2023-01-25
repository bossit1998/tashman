package uz.tm.tashman.enums;

import uz.tm.tashman.models.HashMapModel;

import java.util.ArrayList;

public enum ProductCategory {
    SHAMPOO(0, "shampoo", "Shampoo", "Шампунь", "Shampun"),
    LIQUID_SOAP(1, "liquid_soap", "Liquid soap", "Жидкое мыло", "Suyuq sovun"),
    DISHWASHING_LIQUID(2, "dishwashing_liquid", "Dishwashing liquid", "Моющее средство", "Idish yuvish vositasi"),
    KIDS_SHAMPOO(3, "kids_shampoo", "Kids shampoo", "Детский шампунь", "Bolalar shampuni");

    private final long id;
    private final String code;
    private final String englishName;
    private final String russianName;
    private final String uzbekName;

    ProductCategory(int id, String code, String englishName, String russianName, String uzbekName) {
        this.id = id;
        this.code = code;
        this.englishName = englishName;
        this.russianName = russianName;
        this.uzbekName = uzbekName;
    }

    public static ProductCategory getByCode(String value) {
        for (ProductCategory target : values()) {
            if (target.name().equals(value)) {
                return target;
            }
        }
        return null;
    }

    public static ProductCategory getById(long id) {
        for (ProductCategory target : values()) {
            if (target.id == id) {
                return target;
            }
        }
        return null;
    }


    public static ArrayList<HashMapModel> getAllByLanguage(Language language) {
        ArrayList<HashMapModel> result = new ArrayList<>();
        switch (language) {
            case EN:
                for (ProductCategory target : values()) {
                    HashMapModel hashMapModel = new HashMapModel();
                    hashMapModel.setLabel(target.englishName);
                    hashMapModel.setValue(target.code);
                    result.add(hashMapModel);
                }
                return result;
            case UZ:
                for (ProductCategory target : values()) {
                    HashMapModel hashMapModel = new HashMapModel();
                    hashMapModel.setLabel(target.uzbekName);
                    hashMapModel.setValue(target.code);
                    result.add(hashMapModel);
                }
                return result;
            case RU:
            default:
                for (ProductCategory target : values()) {
                    HashMapModel hashMapModel = new HashMapModel();
                    hashMapModel.setLabel(target.russianName);
                    hashMapModel.setValue(target.code);
                    result.add(hashMapModel);
                }
                return result;
        }
    }
}