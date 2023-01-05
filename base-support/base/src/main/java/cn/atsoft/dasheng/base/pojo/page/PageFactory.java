package cn.atsoft.dasheng.base.pojo.page;

import cn.atsoft.dasheng.base.model.Sorter;
import cn.atsoft.dasheng.core.util.HttpContext;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Table默认的分页参数创建
 */
public class PageFactory {

    /**
     * 获取 table的分页参数
     */
    public static <T> Page<T> defaultPage() {
        return PageFactory.defaultPage(new ArrayList<>());
    }

    /**
     * @param fields 可排序的字段，不在这里的不参与排序。过滤字段用
     * @param <T>
     * @return
     */
    public static <T> Page<T> defaultPage(List<String> fields) {
        HttpServletRequest request = HttpContext.getRequest();

        int limit = 20;
        int page = 1;
        Sorter sorter = new Sorter();
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

        String sortString = request.getParameter("sorter");
        if (ToolUtil.isNotEmpty(sortString)) {
            // page = Integer.parseInt(pageString);
            try {
                sorter = JSON.parseObject(sortString, Sorter.class);
            } catch (Exception e) {

            }
        }
        Page<T> pageObj = new Page<T>(page, limit);
        // 统一翻页排序
        List<OrderItem> orderItemList = new ArrayList<>();
        if (ToolUtil.isNotEmpty(sorter.getOrder()) && ToolUtil.isNotEmpty(sorter.getField())) {
            for (String item : fields) {
                if (sorter.getField().equals(item)) {
                    if (sorter.getOrder().equals("descend")) {
                        orderItemList = OrderItem.descs(sorter.getField());
                    }
                    if (sorter.getOrder().equals("ascend")) {
                        orderItemList = OrderItem.ascs(sorter.getField());
                    }
                }
            }
        }
        if (orderItemList.stream().noneMatch(i->i.getColumn().equals("createTime"))){
            sorter.setField("createTime");
            orderItemList = OrderItem.descs(sorter.getField());
        }


        pageObj.setOrders(orderItemList);
        return pageObj;
    }

    /**
     * 创建能识别的分页响应参数
     */
    public static <T> PageInfo<T> createPageInfo(IPage<T> page) {
        PageInfo<T> result = new PageInfo<T>();
        result.setCount(page.getTotal());
        result.setData(page.getRecords());
        result.setCurrent(page.getCurrent());
        result.setPageSize(page.getSize());
        return result;
    }
}
