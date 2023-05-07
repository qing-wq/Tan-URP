package ink.whi.api.model.context;

import ink.whi.api.model.dto.BaseUserInfoDTO;
import lombok.Data;

/**
 * @author: qing
 * @Date: 2023/5/7
 */
public class ReqInfoContext {

    /**
     * 本地线程变量
     */
    private static ThreadLocal<ReqInfo> contexts = new ThreadLocal<>();

    public static void addReqInfo(ReqInfo reqInfo) {
        contexts.set(reqInfo);
    }

    public static void clear() {
        contexts.remove();
    }

    public static ReqInfo getReqInfo() {
        return contexts.get();
    }

    @Data
    public static class ReqInfo {
        /**
         * 客户端ip
         */
        private String clientIp;
        /**
         * 访问路径
         */
        private String path;
        /**
         * referer
         */
        private String referer;
        /**
         * 设备信息
         */
        private String UserAgent;
        /**
         * post 表单参数
         */
        private String payload;
        /**
         * 用户id
         */
        private Long userId;
        /**
         * 用户信息
         */
        private BaseUserInfoDTO user;
    }
}