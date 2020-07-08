package com.baseframework.config.poi.excel;


import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import static com.baseframework.config.poi.excel.ExcelUtil.getReplaceAttributeMap;

/**
 * @author 邵锦杰
 * @time 2019/3/20
 * @description ${description}
 */
public class ObjectUtil {

    private static DateTimeFormatter ld = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static DateTimeFormatter ldt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    //对象是否存在此属性
    protected static boolean existsField(Class clz, String fieldName) {
        Field[] fields = clz.getDeclaredFields();
        boolean result = false;
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getName().equals(fieldName)) {
                result = true;
                break;
            }
        }
        return result;
    }

    //获取对象中的属性
    protected static Field getField(Class clz, String fieldName) {
        while (clz != null) {
            if (existsField(clz, fieldName)) {
                try {
                    return clz.getDeclaredField(fieldName);
                } catch (NoSuchFieldException e) {
                    return null;
                }
            } else {
                clz = clz.getSuperclass();
            }
        }
        return null;
    }

    //设置对象中的属性值
    protected static ConvertResult setDate(Object obj, Object data, String fieldName) throws IllegalAccessException {
        Field f = getField(obj.getClass(), fieldName);
        if (f == null) return null;
        f.setAccessible(true);
        String type = f.getGenericType().toString();
        Map<String, String> replaceAttributeMap = getReplaceAttributeMap(obj.getClass(), fieldName, true);
        ConvertResult convertResult = replaceAttributeMap.size() == 0 ? convert(data, type) : convert(data, type, replaceAttributeMap);
        if (convertResult.getSuccess()) {
            f.set(obj, convertResult.getData());
        } else {

        }
        return convertResult;
    }

    //值映射
    protected static ConvertResult convert(Object data, String targetType) {
        Object result = null;
        if (data instanceof Double) {
            switch (targetType) {
                case "class java.lang.String": {
                    try {
                        result = String.valueOf(data);
                    } catch (Exception e) {
                        return ConvertResult.fail("必须为字符串");
                    }
                    break;
                }
                case "class java.lang.Long": {
                    try {
                        result = Long.parseLong(String.valueOf(data));
                    } catch (Exception e) {
                        return ConvertResult.fail("必须为整数");
                    }
                    break;
                }
                case "class java.lang.Integer": {
                    try {
                        result = Integer.valueOf(String.valueOf(data));
                    } catch (Exception e) {
                        return ConvertResult.fail("必须为整数");
                    }
                    break;
                }
                case "class java.time.LocalDate": {
                    try {
                        result = LocalDate.parse(String.valueOf(data), ld);
                    } catch (Exception e) {
                        return ConvertResult.fail("必须为yyyy-mm-dd格式，例如2000-01-01");
                    }
                    break;
                }
                case "class java.time.LocalDateTime": {
                    try {
                        result = LocalDateTime.parse(String.valueOf(data), ldt);
                    } catch (Exception e) {
                        return ConvertResult.fail("必须为yyyy-mm-dd MM:HH:ss格式，例如2000-01-01 11:11:11");
                    }
                    break;
                }
                case "class java.lang.Float": {
                    try {
                        result = (Float) data;
                    } catch (Exception e) {
                        return ConvertResult.fail("必须为数字");
                    }
                    break;
                }
                case "class java.lang.Double": {
                    try {
                        result = (Double) data;
                    } catch (Exception e) {
                        return ConvertResult.fail("必须为数字");
                    }
                    break;
                }
            }
        } else if (data instanceof String) {
            switch (targetType) {
                case "class java.lang.String": {
                    result = data;
                    break;
                }
                case "class java.lang.Integer": {
                    try {
                        result = Integer.valueOf(String.valueOf(data));
                    } catch (Exception e) {
                        return ConvertResult.fail("必须为整数");
                    }
                    break;
                }
                case "class java.lang.Long": {
                    try {
                        result = Long.parseLong(String.valueOf(data));
                    } catch (Exception e) {
                        return ConvertResult.fail("必须为整数");
                    }
                    break;
                }
                case "class java.time.LocalDate": {
                    try {
                        result = LocalDate.parse(String.valueOf(data), ld);
                    } catch (Exception e) {
                        return ConvertResult.fail("必须为yyyy-mm-dd格式，例如2000-01-01");
                    }
                    break;
                }
                case "class java.time.LocalDateTime": {
                    try {
                        result = LocalDateTime.parse(String.valueOf(data), ldt);
                    } catch (Exception e) {
                        return ConvertResult.fail("必须为yyyy-mm-dd MM:HH:ss格式，例如2000-01-01 11:11:11");
                    }
                    break;
                }
                case "class java.lang.Float": {
                    try {
                        result = Float.valueOf(String.valueOf(data));
                    } catch (Exception e) {
                        return ConvertResult.fail("必须为数字");
                    }
                    break;
                }
                case "class java.lang.Double": {
                    try {
                        result = Double.valueOf(String.valueOf(data));
                    } catch (Exception e) {
                        return ConvertResult.fail("必须为数字");
                    }
                    break;
                }
            }
        } else if (data instanceof Boolean) {

        } else if (data instanceof Byte) {

        }
        return ConvertResult.success(result);
    }

    // 字段replace映射
    public static ConvertResult convert(Object data, String targetType, Map<String, String> replaceAttributeMap) {
        Object result = null;
        if (data instanceof Double) {
            result = String.valueOf(data);
        } else if (data instanceof String) {
            result = data;
        } else if (data instanceof Boolean) {

        } else if (data instanceof Byte) {

        }
        switch (targetType) {
            case "class java.lang.String": {
                if (replaceAttributeMap.containsKey(result)) {
                    return ConvertResult.success(replaceAttributeMap.get(result));
                } else {
                    return ConvertResult.fail("必须为:" + StringUtils.join(replaceAttributeMap.keySet(), "，"));
                }
            }
            case "class java.lang.Integer": {
                if (replaceAttributeMap.containsKey(data)) {
                    return ConvertResult.success(Integer.valueOf(replaceAttributeMap.get(result)));
                } else {
                    return ConvertResult.fail("必须为:" + StringUtils.join(replaceAttributeMap.keySet(), "，"));
                }
            }
        }
        return null;
    }


    static class ConvertResult {
        private Object data;
        private Boolean success;
        private String errMsg;

        public static ConvertResult success(Object data) {
            ConvertResult convertResult = new ConvertResult();
            convertResult.setSuccess(true);
            convertResult.setData(data);
            return convertResult;
        }

        public static ConvertResult fail(String errMsg) {
            ConvertResult convertResult = new ConvertResult();
            convertResult.setSuccess(false);
            convertResult.setErrMsg(errMsg);
            return convertResult;
        }

        public Boolean getSuccess() {
            return success;
        }

        public void setSuccess(Boolean success) {
            this.success = success;
        }

        public String getErrMsg() {
            return errMsg;
        }

        public void setErrMsg(String errMsg) {
            this.errMsg = errMsg;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }
}
