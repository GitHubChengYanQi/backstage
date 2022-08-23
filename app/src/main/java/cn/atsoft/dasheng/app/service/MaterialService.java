package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Material;
import cn.atsoft.dasheng.app.model.params.MaterialParam;
import cn.atsoft.dasheng.app.model.result.MaterialResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 材质 服务类
 * </p>
 *
 * @author 1
 * @since 2021-07-14
 */
public interface MaterialService extends IService<Material> {

    /**
     * 新增
     *
     * @author 1
     * @Date 2021-07-14
     */
    Long add(MaterialParam param);

    /**
     * 删除
     *
     * @author 1
     * @Date 2021-07-14
     */
    void delete(MaterialParam param);

    /**
     * 更新
     *
     * @author 1
     * @Date 2021-07-14
     */
    void update(MaterialParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 1
     * @Date 2021-07-14
     */
    MaterialResult findBySpec(MaterialParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 1
     * @Date 2021-07-14
     */
    List<MaterialResult> findListBySpec(MaterialParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 1
     * @Date 2021-07-14
     */
     PageInfo findPageBySpec(MaterialParam param, DataScope dataScope );

}
