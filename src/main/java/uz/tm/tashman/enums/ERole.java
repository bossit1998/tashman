package uz.tm.tashman.enums;

import lombok.Getter;
import lombok.NonNull;

@Getter
public enum ERole {
    ROLE_USER(0, "ROLE_USER"),
    ROLE_ADMIN(0, "ROLE_ADMIN"),
    ROLE_SECURITY(0, "ROLE_SECURITY"),
    ROLE_MONITORING(0, "ROLE_MONITORING"),
    ROLE_ACCOUNTANT(0, "ROLE_ACCOUNTANT");

    private final Integer id;
    private final String code;

    ERole(int id, String code) {
        this.id = id;
        this.code = code;
    }

    public static ERole get(String role) {
        for (ERole roles : values()) {
            if (roles.name().equals(role)) {
                return roles;
            }
        }
        return null;
    }

    @NonNull
    public static ERole getById(long id) {
        for (ERole roles : values()) {
            if (roles.getId() == id) {
                return roles;
            }
        }
        return null;
    }
}
