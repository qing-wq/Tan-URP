package ink.whi.web.hook.interceptor;

import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.enums.RoleEnum;
import ink.whi.api.model.exception.StatusEnum;
import ink.whi.api.model.vo.ResVo;
import ink.whi.api.permission.Permission;
import ink.whi.api.permission.UserRole;
import ink.whi.core.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author: qing
 * @Date: 2023/5/7
 */
@Slf4j
@Component
public class GlobalInterceptor implements AsyncHandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行静态资源
        if (staticURI(request)) {
            return true;
        }
        if (handler instanceof HandlerMethod handlerMethod) {
            Permission permission = handlerMethod.getMethod().getAnnotation(Permission.class);
            if (permission == null) {
                permission = handlerMethod.getBeanType().getAnnotation(Permission.class);
            }

            // 未登录
            if (ReqInfoContext.getReqInfo() == null || ReqInfoContext.getReqInfo().getUserId() == null) {
                response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.getWriter().println(JsonUtil.toStr(ResVo.fail(StatusEnum.FORBID_ERROR_MIXED, "请登录")));
                response.getWriter().flush();
                return false;
            }

            // ALL
            if (permission == null || permission.role() == UserRole.ALL) {
                return true;
            }

            // Leader
            if (permission.role() == UserRole.LEADER && Objects.equals(ReqInfoContext.getReqInfo().getUser().getRole().getRoleId(), RoleEnum.NORMAL.getRole())) {
                response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.getWriter().println(JsonUtil.toStr(ResVo.fail(StatusEnum.FORBID_ERROR)));
                response.getWriter().flush();
                return false;
            }

            // Admin
            if (permission.role() == UserRole.ADMIN && !Objects.equals(ReqInfoContext.getReqInfo().getUser().getRole().getRoleId(), RoleEnum.ADMIN.getRole())) {
                response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.getWriter().println(JsonUtil.toStr(ResVo.fail(StatusEnum.FORBID_ERROR)));
                response.getWriter().flush();
                return false;
            }
        }
        return true;
    }

    private boolean staticURI(HttpServletRequest request) {
        return request == null
                || request.getRequestURI().endsWith("css")
                || request.getRequestURI().endsWith("js")
                || request.getRequestURI().endsWith("png")
                || request.getRequestURI().endsWith("ico")
                || request.getRequestURI().endsWith("svg");
    }
}
