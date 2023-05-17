package ink.whi.web.hook.filter;

import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.util.CrossUtil;
import ink.whi.web.global.GlobalInitHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * 鉴权Filter
 * @author: qing
 * @Date: 2023/5/6
 */
@Slf4j
@WebFilter(urlPatterns = "/*", filterName = "authFilter", asyncSupported = true)
public class AuthFilter implements Filter {
    private static final Logger REQ_LOG = LoggerFactory.getLogger("req");

    @Resource
    private GlobalInitHelper globalInitService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        long start = System.currentTimeMillis();
        HttpServletRequest req = null;
        try {
            req = initReqInfo((HttpServletRequest) request);
            CrossUtil.buildCors(req, (HttpServletResponse) response);
            chain.doFilter(req, response);
        }finally {
            buildRequestLog(ReqInfoContext.getReqInfo(), req, System.currentTimeMillis() - start);
            ReqInfoContext.clear();
        }
    }

    private HttpServletRequest initReqInfo(HttpServletRequest request) {
        if (staticURI(request)) {
            // 静态资源直接放行
            return request;
        }

        try {
            ReqInfoContext.ReqInfo reqInfo = new ReqInfoContext.ReqInfo();
            reqInfo.setPath(request.getPathInfo());
            reqInfo.setReferer(request.getHeader("referer"));
            reqInfo.setUserAgent(request.getHeader("User-Agent"));
            request = this.wrapperRequest(request, reqInfo);
            // 校验token
            globalInitService.initUserInfo(reqInfo);
            ReqInfoContext.addReqInfo(reqInfo);
        } catch (Exception e) {
            log.error("init reqInfo error: " + e.getMessage());
        }
        return request;
    }

    private HttpServletRequest wrapperRequest(HttpServletRequest request, ReqInfoContext.ReqInfo reqInfo) {
        BodyReaderHttpServletRequestWrapper requestWrapper = new BodyReaderHttpServletRequestWrapper(request);
        reqInfo.setPayload(requestWrapper.getBodyString());
        return requestWrapper;
    }

    /**
     * 日志输出
     * @param req
     * @param request
     * @param costTime
     */
    private void buildRequestLog(ReqInfoContext.ReqInfo req, HttpServletRequest request, long costTime) {
        if (req == null || staticURI(request)) {
            return;
        }

        StringBuilder msg = new StringBuilder();
        msg.append("method=").append(request.getMethod()).append("; ");
        if (StringUtils.isNotBlank(req.getReferer())) {
            msg.append("referer=").append(URLDecoder.decode(req.getReferer())).append("; ");
        }
        msg.append(req.getUserAgent());

        if (req.getUserId() != null) {
            // 打印用户信息
            msg.append("; user=").append(req.getUserId());
        }

        msg.append("; uri=").append(request.getRequestURI());
        if (StringUtils.isNotBlank(request.getQueryString())) {
            msg.append('?').append(URLDecoder.decode(request.getQueryString()));
        }

        msg.append("; payload=").append(req.getPayload());
        msg.append("; cost=").append(costTime);
        log.info("{}", msg);

        // todo: 保存请求计数
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
