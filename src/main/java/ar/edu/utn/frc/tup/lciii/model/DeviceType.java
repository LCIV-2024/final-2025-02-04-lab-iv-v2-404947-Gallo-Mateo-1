package ar.edu.utn.frc.tup.lciii.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DeviceType {
    LAPTOP, DESKTOP, TABLET, SMARTPHONE;

    @JsonCreator
    public static DeviceType fromString(String value) {
        return DeviceType.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toJson() {
        return this.name();
    }
}
