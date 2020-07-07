package com.baseframework.pojo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Objects;


@Getter
public enum ResultCodeEnum {

    SUCCESS(0, "成功"), FAIL(1, "失败");

    ResultCodeEnum(int code, String descp) {
        this.code = code;
        this.descp = descp;
    }


    @EnumValue
    private final Integer code;

    private final String descp;


    @JsonCreator
    public static ResultCodeEnum getEnum(int code) {
        for (ResultCodeEnum value : ResultCodeEnum.values()) {
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
