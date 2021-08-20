package cn.atsoft.dasheng.portal.companyaddress.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.companyaddress.entity.CompanyAddress;
import cn.atsoft.dasheng.portal.companyaddress.model.params.CompanyAddressParam;
import cn.atsoft.dasheng.portal.companyaddress.model.result.CompanyAddressResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 报修 服务类
 * </p>
 *
 * @author siqiang
 * @since 2021-08-20
 */
public interface CompanyAddressService extends IService<CompanyAddress> {

    /**
     * 新增
     *
     * @author siqiang
     * @Date 2021-08-20
     */
    void add(CompanyAddressParam param);

    /**
     * 删除
     *
     * @author siqiang
     * @Date 2021-08-20
     */
    void delete(CompanyAddressParam param);

    /**
     * 更新
     *
     * @author siqiang
     * @Date 2021-08-20
     */
    void update(CompanyAddressParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author siqiang
     * @Date 2021-08-20
     */
    CompanyAddressResult findBySpec(CompanyAddressParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author siqiang
     * @Date 2021-08-20
     */
    List<CompanyAddressResult> findListBySpec(CompanyAddressParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author siqiang
     * @Date 2021-08-20
     */
     PageInfo<CompanyAddressResult> findPageBySpec(CompanyAddressParam param);

}
