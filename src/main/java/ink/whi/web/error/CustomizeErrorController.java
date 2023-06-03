package ink.whi.web.error;

import ink.whi.api.model.exception.StatusEnum;
import ink.whi.api.model.vo.ResVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author qing
 * @date 2023/6/3
 */
@Slf4j
@RestController("/error")
@RequestMapping("${server.error.path:${error.path:/error}}")
public class CustomizeErrorController implements ErrorController {

    @RequestMapping(path = "")
    public ResVo<String> error(HttpServletRequest request) {
        HttpStatus status = getStatus(request);
        log.error("[Error] capture an Error");

        if (status.is4xxClientError()) {
            ResVo.fail(StatusEnum.BAD_REQUEST);
        }
        return ResVo.fail(StatusEnum.UNEXPECT_ERROR);
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        } catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}