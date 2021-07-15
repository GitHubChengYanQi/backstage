package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Client;
import cn.atsoft.dasheng.app.model.params.ClientParam;
import cn.atsoft.dasheng.app.model.result.ClientResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 客户表 服务类
 * </p>
 *
 * @author 
 * @since 2021-07-15
 */
public interface ClientService extends IService<Client> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-07-15
     */
    void add(ClientParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-07-15
     */
    void delete(ClientParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-07-15
     */
    void update(ClientParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-07-15
     */
    ClientResult findBySpec(ClientParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-07-15
     */
    List<ClientResult> findListBySpec(ClientParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-07-15
     */
     PageInfo<ClientResult> findPageBySpec(ClientParam param);

}
