package cn.atsoft.dasheng.crm.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.CustomerFile;
import cn.atsoft.dasheng.crm.model.params.CustomerFileParam;
import cn.atsoft.dasheng.crm.model.result.CustomerFileResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2021-09-08
 */
public interface CustomerFileService extends IService<CustomerFile> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-09-08
     */
    void add(CustomerFileParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-09-08
     */
    void delete(CustomerFileParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-09-08
     */
    void update(CustomerFileParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-09-08
     */
    CustomerFileResult findBySpec(CustomerFileParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-09-08
     */
    List<CustomerFileResult> findListBySpec(CustomerFileParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-09-08
     */
     PageInfo<CustomerFileResult> findPageBySpec(CustomerFileParam param);

}
