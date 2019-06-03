package com.example.server.manager.common.protocol;

import com.example.server.manager.common.enums.ResultEnum;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 定义服务数据返回格式
 *
 * @date 2017-10-01
 */
public class ActionResult {
    /**
     * 返回状态编码
     */
    private String code = "0";
    /**
     * 返回消息
     */
    private String msg;
    /**
     * 容器
     */
    private Map<String, Object> container = new HashMap<String, Object>();

    public ActionResult() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setResultEnum(ResultEnum resultEnum) {
        this.code = resultEnum.getCode();
        if (resultEnum.getMsg() != null) {
            this.msg = resultEnum.getMsg();
        }
    }

    /**
     * 向Result中添加自定义的结果
     */
    public void addResult(String key, Object value) {
        container.put(key, value);
    }

    public void removeResult(String key) {
        container.remove(key);
    }

    public boolean containsKey(String key) {
        return container.containsKey(key);
    }

    public Map<String, Object> getResultMap() {
        container.put("code", this.code);
        if (StringUtils.isNotBlank(this.msg)) {
            container.put("msg", this.msg);
        }
        return this.container;
    }
}
