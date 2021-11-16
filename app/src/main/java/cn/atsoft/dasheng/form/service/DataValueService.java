package cn.atsoft.dasheng.form.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.DataValue;
import cn.atsoft.dasheng.form.model.params.DataValueParam;
import cn.atsoft.dasheng.form.model.result.DataValueResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 自定义表单各个字段数据	 服务类
 * </p>
 *
 * @author song
 * @since 2021-11-16
 */
public interface DataValueService extends IService<DataValue> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-11-16
     */
    void add(DataValueParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-11-16
     */
    void delete(DataValueParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-11-16
     */
    void update(DataValueParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-11-16
     */
    DataValueResult findBySpec(DataValueParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-11-16
     */
    List<DataValueResult> findListBySpec(DataValueParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-11-16
     */
     PageInfo<DataValueResult> findPageBySpec(DataValueParam param);

}
