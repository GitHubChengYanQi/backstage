package cn.atsoft.dasheng.crm.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Data;
import cn.atsoft.dasheng.crm.model.params.DataParam;
import cn.atsoft.dasheng.crm.model.result.DataResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 资料 服务类
 * </p>
 *
 * @author song
 * @since 2021-09-11
 */
public interface DataService extends IService<Data> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-09-11
     */
    void add(DataParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-09-11
     */
    void delete(DataParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-09-11
     */
    void update(DataParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-09-11
     */
    DataResult findBySpec(DataParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-09-11
     */
    List<DataResult> findListBySpec(DataParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-09-11
     */
    PageInfo<DataResult> findPageBySpec(DataParam param);


    DataResult detail(DataParam param);

    void batchDelete(List<Long> ids);

}
