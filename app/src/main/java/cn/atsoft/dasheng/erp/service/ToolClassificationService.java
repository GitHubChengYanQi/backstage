package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.ToolClassification;
import cn.atsoft.dasheng.erp.model.params.ToolClassificationParam;
import cn.atsoft.dasheng.erp.model.result.ToolClassificationResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 工具分类表 服务类
 * </p>
 *
 * @author song
 * @since 2021-10-23
 */
public interface ToolClassificationService extends IService<ToolClassification> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-10-23
     */
    void add(ToolClassificationParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-10-23
     */
    void delete(ToolClassificationParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-10-23
     */
    void update(ToolClassificationParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-10-23
     */
    ToolClassificationResult findBySpec(ToolClassificationParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-10-23
     */
    List<ToolClassificationResult> findListBySpec(ToolClassificationParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-10-23
     */
     PageInfo<ToolClassificationResult> findPageBySpec(ToolClassificationParam param);

}
