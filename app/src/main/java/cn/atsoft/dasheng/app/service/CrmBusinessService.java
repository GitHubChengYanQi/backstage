package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.CrmBusiness;
import cn.atsoft.dasheng.app.model.params.CrmBusinessParam;
import cn.atsoft.dasheng.app.model.result.CrmBusinessResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商机表 服务类
 * </p>
 *
 * @author 
 * @since 2021-08-03
 */
public interface CrmBusinessService extends IService<CrmBusiness> {

    CrmBusinessResult detail(Long id);
    /**
     * 新增
     *
     * @author 
     * @Date 2021-08-03
     * @return
     */
    CrmBusiness add(CrmBusinessParam param);

    /**
     * 更新负责人
     * @param param
     * @return
     */
    CrmBusiness updateChargePerson(CrmBusinessParam param);
    /**
     * 删除
     *
     * @author 
     * @Date 2021-08-03
     * @return
     */
    CrmBusiness delete(CrmBusinessParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-08-03
     * @return
     */
    CrmBusiness update(CrmBusinessParam param);


    String UpdateStatus (CrmBusinessParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-08-03
     */
    CrmBusinessResult findBySpec(CrmBusinessParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-08-03
     */
    List<CrmBusinessResult> findListBySpec(CrmBusinessParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-08-03
     */
     PageInfo findPageBySpec(DataScope dataScope, CrmBusinessParam param);


      void batchDelete(List<Long> businessIds);
}
