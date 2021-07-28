package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Business;
import cn.atsoft.dasheng.app.model.params.BusinessParam;
import cn.atsoft.dasheng.app.model.result.BusinessResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商机表 服务类
 * </p>
 *
 * @author cheng
 * @since 2021-07-19
 */
public interface BusinessService extends IService<Business> {

    /**
     * 新增
     *
     * @author cheng
     * @Date 2021-07-19
     */
    Long add(BusinessParam param);

    /**
     * 删除
     *
     * @author cheng
     * @Date 2021-07-19
     */
    void delete(BusinessParam param);

    /**
     * 更新
     *
     * @author cheng
     * @Date 2021-07-19
     */
    void update(BusinessParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author cheng
     * @Date 2021-07-19
     */
    BusinessResult findBySpec(BusinessParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author cheng
     * @Date 2021-07-19
     */
    List<BusinessResult> findListBySpec(BusinessParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author cheng
     * @Date 2021-07-19
     */
     PageInfo<BusinessResult> findPageBySpec(BusinessParam param);

}
