package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Phone;
import cn.atsoft.dasheng.app.model.params.PhoneParam;
import cn.atsoft.dasheng.app.model.result.PhoneResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cheng
 * @since 2021-08-12
 */
public interface PhoneService extends IService<Phone> {

    /**
     * 新增
     *
     * @author cheng
     * @Date 2021-08-12
     */
    Phone add(PhoneParam param);

    /**
     * 删除
     *
     * @author cheng
     * @Date 2021-08-12
     */
    void delete(PhoneParam param);

    /**
     * 更新
     *
     * @author cheng
     * @Date 2021-08-12
     */
    void update(PhoneParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author cheng
     * @Date 2021-08-12
     */
    PhoneResult findBySpec(PhoneParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author cheng
     * @Date 2021-08-12
     */
    List<PhoneResult> findListBySpec(PhoneParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author cheng
     * @Date 2021-08-12
     */
     PageInfo findPageBySpec(PhoneParam param, DataScope dataScope );

}
