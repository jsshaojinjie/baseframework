package com.baseframework.pojo.common;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baseframework.pojo.enums.EffectiveSignEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 邵锦杰
 */
@Data
public class BaseEntity {
    /**
     * 创建时间
     */
    @JsonIgnore
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @JsonIgnore
    private LocalDateTime updateTime;

    /**
     * 有效标识，0有效、1失效
     */
    @TableLogic
    @JsonIgnore
    private EffectiveSignEnum effectiveSign;
}
