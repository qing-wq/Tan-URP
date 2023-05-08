package ink.whi.web.hook.interceptor;

import com.google.common.collect.Maps;
import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.exception.StatusEnum;
import ink.whi.api.permission.Permission;
import ink.whi.api.permission.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
@Slf4j
@Component
public class GlobalInterceptor implements AsyncHandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod handlerMethod) {
            Permission permission = handlerMethod.getMethod().getAnnotation(Permission.class);
            if (permission == null) {
                permission = handlerMethod.getBeanType().getAnnotation(Permission.class);
            }

            // ALL
            if (permission == null || permission.role() == UserRole.ALL) {
                return true;
            }

            // Leader
            if (permission.role() == UserRole.LEADER && ReqInfoContext.getReqInfo().getUserId() == null) {
                response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.getWriter().println(Maps.newHashMap().put(StatusEnum.FORBID_ERROR_MIXED, "没有权限"));
                response.getWriter().flush();
                return false;
            }

            // Admin
            if (permission.role() == UserRole.ADMIN && !UserRole.ADMIN.name().equalsIgnoreCase(ReqInfoContext.getReqInfo().getUser().getRole())) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        ReqInfoContext.clear();
    }
}
