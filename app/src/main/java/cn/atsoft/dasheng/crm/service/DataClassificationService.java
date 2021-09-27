package cn.atsoft.dasheng.crm.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.DataClassification;
import cn.atsoft.dasheng.crm.model.params.DataClassificationParam;
import cn.atsoft.dasheng.crm.model.result.DataClassificationResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 资料分类表 服务类
 * </p>
 *
 * @author 
 * @since 2021-09-13
 */
public interface DataClassificationService extends IService<DataClassification> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-09-13
     */
    void add(DataClassificationParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-09-13
     */
    void delete(DataClassificationParam param);

    /**
     * 批量删除
     *
     * @author
     * @Date 2021-09-13
     */
    void batchDelete(List<Long> ids);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-09-13
     */
    void update(DataClassificationParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-09-13
     */
    DataClassificationResult findBySpec(DataClassificationParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-09-13
     */
    List<DataClassificationResult> findListBySpec(DataClassificationParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-09-13
     */
     PageInfo<DataClassificationResult> findPageBySpec(DataClassificationParam param);

}
