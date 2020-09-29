
package com.baseframework.pojo.common;

import com.baseframework.pojo.enums.ResultCodeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @param <T>
 * @author 邵锦杰
 */
@Builder
@ToString
@Accessors(chain = true)
@AllArgsConstructor
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private ResultCodeEnum code = ResultCodeEnum.SUCCESS;

    @Getter
    @Setter
    private String msg = "success";

    @Getter
    @Setter
    private T data;

    public R() {
        super();
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.code == ResultCodeEnum.SUCCESS ? true : false;
    }


    public static <T> R success(T data) {
        return R.builder().code(ResultCodeEnum.SUCCESS).data(data).build();
    }

    public static <T> R fail(T data, String message) {
        return R.builder().code(ResultCodeEnum.FAIL).data(data).msg(message).build();
    }
}
