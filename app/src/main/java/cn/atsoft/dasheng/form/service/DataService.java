package cn.atsoft.dasheng.form.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.Data;
import cn.atsoft.dasheng.form.model.params.DataParam;
import cn.atsoft.dasheng.form.model.result.DataResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 自定义表单主表 服务类
 * </p>
 *
 * @author song
 * @since 2021-11-16
 */
public interface DataService extends IService<Data> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-11-16
     */
    void add(DataParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-11-16
     */
    void delete(DataParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-11-16
     */
    void update(DataParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-11-16
     */
    DataResult findBySpec(DataParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-11-16
     */
    List<DataResult> findListBySpec(DataParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-11-16
     */
     PageInfo<DataResult> findPageBySpec(DataParam param);

}
