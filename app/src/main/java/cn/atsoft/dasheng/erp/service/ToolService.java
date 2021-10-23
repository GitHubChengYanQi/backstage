package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Tool;
import cn.atsoft.dasheng.erp.model.params.ToolParam;
import cn.atsoft.dasheng.erp.model.result.ToolResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 工具表 服务类
 * </p>
 *
 * @author song
 * @since 2021-10-23
 */
public interface ToolService extends IService<Tool> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-10-23
     */
    void add(ToolParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-10-23
     */
    void delete(ToolParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-10-23
     */
    void update(ToolParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-10-23
     */
    ToolResult findBySpec(ToolParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-10-23
     */
    List<ToolResult> findListBySpec(ToolParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-10-23
     */
     PageInfo<ToolResult> findPageBySpec(ToolParam param);

}
