package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.app.model.params.BusinessDetailedParam;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.CrmBusinessDetailed;
import cn.atsoft.dasheng.app.model.params.CrmBusinessDetailedParam;
import cn.atsoft.dasheng.app.model.result.CrmBusinessDetailedResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商机明细表 服务类
 * </p>
 *
 * @author qr
 * @since 2021-08-04
 */
public interface CrmBusinessDetailedService extends IService<CrmBusinessDetailed> {

    /**
     * 新增
     *
     * @author qr
     * @Date 2021-08-04
     */
    void add(CrmBusinessDetailedParam param);

    /**
     * 批量增加
     */
    void addAll(BusinessDetailedParam param);
    /**
     * 批量增加
     */
    void addAllPackages(CrmBusinessDetailedParam param);
    /**
     * 删除
     *
     * @author qr
     * @Date 2021-08-04
     */
    void delete(CrmBusinessDetailedParam param);

    /**
     * 更新
     *
     * @author qr
     * @Date 2021-08-04
     */
    void update(CrmBusinessDetailedParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author qr
     * @Date 2021-08-04
     */
    CrmBusinessDetailedResult findBySpec(CrmBusinessDetailedParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author qr
     * @Date 2021-08-04
     */
    List<CrmBusinessDetailedResult> findListBySpec(CrmBusinessDetailedParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author qr
     * @Date 2021-08-04
     */
     PageInfo findPageBySpec(CrmBusinessDetailedParam param, DataScope dataScope );

}
