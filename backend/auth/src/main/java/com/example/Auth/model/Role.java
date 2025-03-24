package com.example.Auth.model;

public enum Role {
        ADMIN(0, "ROLE_ADMIN"), USER(1, "ROLE_USER");

        private Integer code;
        private String description;

    Role(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static Role toEnum(String description) {
        if (description == null) {
            return null;
        }
        for (Role role : Role.values()) {
            if (role.getDescription().equals(description)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid Role " + description);
    }

//    public static Role toEnum(Integer code) {
//        if (code == null) {
//            return null;
//        }
//        for (Role x : Role.values()) {
//            if (code.equals(x.getCode())) {
//                return x;
//            }
//        }
//        throw new IllegalArgumentException("Invalid Role " + code);
//    }
}
