package com.example.server.manager.common.util;

import com.example.server.manager.common.enums.ResultEnum;
import com.example.server.manager.common.exception.ServiceException;
import com.example.server.manager.common.protocol.ActionResult;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @date 2017-10-01
 */
public class ResultTool {

    /**
     * 成功时的返回结果
     * <p>
     * Controller:
     * <pre>
     * &#64;GetMapping("sayHello")
     * public Object sayHello() {
     *     return ResultTool.success();
     * }
     * </pre>
     *
     * @return Map
     */
    public static Map<String, Object> success() {
        return new ActionResult().getResultMap();
    }

    /**
     * 成功时的返回结果
     * <p>
     * Controller:
     * <pre>
     * &#64;GetMapping("sayHello")
     * public Object sayHello() {
     *     ActionResult result = new ActionResult(); ...
     *     return ResultTool.success(result);
     * }
     * </pre>
     *
     * @param result
     * @return Map
     */
    public static Map<String, Object> success(ActionResult result) {
        return result.getResultMap();
    }

    /**
     * 失败时的返回结果
     * <p>
     * Controller:
     * <pre>
     * &#64;GetMapping("sayHello")
     * public Object sayHello() {
     *     try { ...
     *         return ResultTool.error();
     *     } catch (Throwable e) { ...
     *     }
     * }
     * </pre>
     *
     * @return Map
     */
    public static Map<String, Object> error() {
        ActionResult result = new ActionResult();
        result.setResultEnum(ResultEnum.ERROR);
        return result.getResultMap();
    }

    /**
     * 失败时的返回结果
     * <p>
     * Controller:
     * <pre>
     * &#64;GetMapping("sayHello")
     * public Object sayHello() {
     *     try { ...
     *         return ResultTool.error(ResultEnum.ERROR);
     *     } catch (Throwable e) { ...
     *     }
     * }
     * </pre>
     *
     * @param resultEnum 返回码枚举
     * @return Map
     */
    public static Map<String, Object> error(ResultEnum resultEnum) {
        if (resultEnum == null) {
            throw new RuntimeException("ResultEnum is null.");
        }
        ActionResult result = new ActionResult();
        result.setResultEnum(resultEnum);
        return result.getResultMap();
    }

    /**
     * 失败时的返回结果
     * <p>
     * Controller:
     * <pre>
     * &#64;GetMapping("sayHello")
     * public Object sayHello() {
     *     try { ...
     *         return ResultTool.error(ResultEnum.ERROR, "appendMessage");
     *     } catch (Throwable e) { ...
     *     }
     * }
     * </pre>
     *
     * @param resultEnum    返回码枚举
     * @param appendMessage 追加的返回消息
     * @return Map
     */
    public static Map<String, Object> error(ResultEnum resultEnum, String appendMessage) {
        if (resultEnum == null) {
            throw new RuntimeException("ResultEnum is null.");
        }
        ActionResult result = new ActionResult();
        result.setCode(resultEnum.getCode());
        StringBuilder builder = new StringBuilder();
        builder.append(resultEnum.getMsg());
        builder.append(", ");
        builder.append(appendMessage);
        result.setMsg(builder.toString());
        return result.getResultMap();
    }

    /**
     * 失败时的返回结果
     * <p>
     * Controller:
     * <pre>
     * &#64;GetMapping("sayHello")
     * public Object sayHello() {
     *     try { ...
     *         return ResultTool.error(ResultEnum.ERROR.getCode(), "error");
     *     } catch (Throwable e) { ...
     *     }
     * }
     * </pre>
     *
     * @param code    返回状态编码
     * @param message 返回消息
     * @return Map
     */
    public static Map<String, Object> error(String code, String message) {
        if (StringUtils.isBlank(code)) {
            throw new RuntimeException("code is null.");
        }
        ActionResult result = new ActionResult();
        result.setCode(code);
        result.setMsg(message);
        return result.getResultMap();
    }

    /**
     * 处理指定异常的返回结果
     * <p>
     * Service:
     * <pre>
     * throw new ServiceException(e);
     * </pre>
     * <p>
     * Controller:
     * <pre>
     * &#64;GetMapping("sayHello")
     * public Object sayHello() {
     *     try {
     *         ...
     *     } catch (Throwable e) {
     *         log.error("Controller.sayHello error", e);
     *         return ResultTool.exception(e);
     *     }
     * }
     * </pre>
     *
     * @param e
     * @return Map
     */
    public static Map<String, Object> exception(Throwable e) {
        ActionResult result = new ActionResult();
        if (e != null && (e instanceof ServiceException)) {
            result.setCode(ResultEnum.EXCEPTION.getCode());
            result.setMsg("ServiceException Message: " + e.getMessage());
            return result.getResultMap();
        }
        result.setCode(ResultEnum.EXCEPTION.getCode());
        result.setMsg(e == null ?
                "Throwable is null" : "Exception Message: " + e.getMessage());
        return result.getResultMap();
    }

//    public static void main(String[] args) {
//        try {
//            //模拟Service层抛出异常
//            try {
//                int i =1/0;
//            } catch (Exception e) {
//                throw new ServiceException(e);
//            }
//            //模拟Controller层捕获异常
//        } catch (Throwable e) {
//            e.printStackTrace();
//            //模拟@ResponseBody输出json至客户端
//            System.out.println(com.alibaba.fastjson.JSON.toJSONString(ResultTool.exception(e)));
//        }
//    }
}
