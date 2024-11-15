package gps.base.model;


import com.fasterxml.jackson.annotation.JsonCreator;

public enum GymCategory {
    physics("피트니스 및 헬스장"),
    water("수영"),
    ball("구기종목"),
    battle("격투기");

    private final String display;

    GymCategory(String displayName) {
        this.display = displayName;
    }

    public String getDisplayName() {
        return display;
    }

    @JsonCreator  // JSON을 ENUM으로 변환 시 사용
    public static GymCategory fromValue(String value) {
        if (value == null) {
            return null;
        }

        for (GymCategory category : GymCategory.values()) {
            if (category.display.equals(value.toLowerCase())) {  // 소문자로 변환하여 비교
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown category: " + value);
    }
}