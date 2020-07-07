package com.baseframework.pojo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Objects;


@Getter
public enum EffectiveSignEnum {

    YES(0, "有效"), NO(1, "无效");

    EffectiveSignEnum(int code, String descp) {
        this.code = code;
        this.descp = descp;
    }


    @EnumValue
    private final Integer code;

    private final String descp;


    @JsonCreator
    public static EffectiveSignEnum getEnum(int code) {
        for (EffectiveSignEnum value : EffectiveSignEnum.values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(code);
    }
}
