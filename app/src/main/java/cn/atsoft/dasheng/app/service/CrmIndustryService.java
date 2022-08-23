package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.CrmIndustry;
import cn.atsoft.dasheng.app.model.params.CrmIndustryParam;
import cn.atsoft.dasheng.app.model.result.CrmIndustryResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 行业表 服务类
 * </p>
 *
 * @author 
 * @since 2021-08-02
 */
public interface CrmIndustryService extends IService<CrmIndustry> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-08-02
     */
    void add(CrmIndustryParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-08-02
     */
    void delete(CrmIndustryParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-08-02
     */
    void update(CrmIndustryParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-08-02
     */
    CrmIndustryResult findBySpec(CrmIndustryParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-08-02
     */
    List<CrmIndustryResult> findListBySpec(CrmIndustryParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-08-02
     */
     PageInfo findPageBySpec(CrmIndustryParam param, DataScope dataScope );


     void batchDelete(List<Long>ids);

}
