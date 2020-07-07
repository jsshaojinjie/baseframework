
package com.baseframework.pojo.common;

/**
 * @author yaomajor
 * @date 2018/10/29
 */
public interface CommonConstants {
    /**
     * header 中租户ID
     */
    String TENANT_ID = "TENANT_ID";
    /**
     * 删除
     */
    String STATUS_DEL = "1";
    /**
     * 正常
     */
    String STATUS_NORMAL = "0";

    /**
     * 锁定
     */
    String STATUS_LOCK = "9";

    /**
     * 菜单
     */
    String MENU = "0";

    /**
     * 编码
     */
    String UTF8 = "UTF-8";

    /**
     * 前端工程名
     */
    String FRONT_END_PROJECT = "industry-ui";

    /**
     * 后端工程名
     */
    String BACK_END_PROJECT = "industry";

    /**
     * 路由存放
     */
    String ROUTE_KEY = "gateway_route_key";

    /**
     * spring boot admin 事件key
     */
    String EVENT_KEY = "event_key";

    /**
     * 验证码前缀
     */
    String DEFAULT_CODE_KEY = "DEFAULT_CODE_KEY_";

    /**
     * 成功标记
     */
    Integer SUCCESS = 0;
    /**
     * 失败标记
     */
    Integer FAIL = 1;

    /**
     * 导入失败标记
     */
    Integer FAIL_IMPORT = 2;
    /**
     * 判断权限标记
     */
    Integer RIGHT = 3;

    /**
     * BUCKET状态值
     */
    String BUCKET_STATE = "upload_file_name";


	/**
	 * 数据库查询effective_sign有效值
	 */
	Integer EFFECTIVESIGN = 0;

	Integer UNEFFECTIVESIGN = 1;
    /**
     * 常数一
     */
    Integer ONE = 1;

    //存放登陆错误时的次数key
    String AUTHFAILUREKEYPREKEY = "authenticationFaileure_";
    //存放符合次数时的上锁 key
    String AUTHSETLOCKKEY = "authenticationSetLock_";
    //存放定时解锁key
    String AUTHEXPIRELOCKKEY = "authenticationExpireLock_";
    //存放定时退出的key
    String LOGOUTKEY = "redisTemplate_";
}
