package ink.whi.api.model.base;

import ink.whi.api.model.vo.PageParam;

/**
 * @author: qing
 * @Date: 2023/5/6
 */
public class PageHelper {

    public PageParam buildPageParam(Long page, Long size) {
        if (page <= 0) {
            page = PageParam.DEFAULT_PAGE_NUM;
        }
        if (size == null || size > PageParam.DEFAULT_PAGE_SIZE) {
            size = PageParam.DEFAULT_PAGE_SIZE;
        }
        return PageParam.newPageInstance(page, size);
    }
}
