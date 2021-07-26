package cn.atsoft.dasheng.base.pojo.page;

import cn.atsoft.dasheng.core.util.HttpContext;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.servlet.http.HttpServletRequest;

/**
 * Table默认的分页参数创建
 */
public class PageFactory {

    /**
     * 获取 table的分页参数
     */
    public static<T> Page<T> defaultPage() {
        HttpServletRequest request = HttpContext.getRequest();

        int limit = 20;
        int page = 1;

        //每页多少条数据
        assert request != null;
        String limitString = request.getParameter("limit");
        if (ToolUtil.isNotEmpty(limitString)) {
            limit = Integer.parseInt(limitString);
        }

        //第几页
        String pageString = request.getParameter("page");
        if (ToolUtil.isNotEmpty(pageString)) {
            page = Integer.parseInt(pageString);
        }

        return new Page<T>(page, limit);
    }



    /**
     * 创建能识别的分页响应参数
     */
    public static<T> PageInfo<T> createPageInfo(IPage<T> page) {
        PageInfo<T> result = new PageInfo<T>();
        result.setCount(page.getTotal());
        result.setData(page.getRecords());
        result.setCurrent(page.getCurrent());
        result.setPageSize(page.getSize());
        return result;
    }
}
