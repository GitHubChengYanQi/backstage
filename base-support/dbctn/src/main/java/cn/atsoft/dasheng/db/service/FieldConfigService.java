package cn.atsoft.dasheng.db.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.db.entity.FieldConfig;
import cn.atsoft.dasheng.db.model.params.FieldConfigParam;
import cn.atsoft.dasheng.db.model.result.FieldConfigResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 字段配置 服务类
 * </p>
 *
 * @author Sing
 * @since 2020-12-11
 */
public interface FieldConfigService extends IService<FieldConfig> {

    /**
     * 新增
     *
     * @author Sing
     * @Date 2020-12-11
     */
    void add(FieldConfigParam param);

    /**
     * 删除
     *
     * @author Sing
     * @Date 2020-12-11
     */
    void delete(FieldConfigParam param);

    /**
     * 更新
     *
     * @author Sing
     * @Date 2020-12-11
     */
    void update(FieldConfigParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Sing
     * @Date 2020-12-11
     */
    FieldConfigResult findBySpec(FieldConfigParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Sing
     * @Date 2020-12-11
     */
    List<FieldConfigResult> findListBySpec(FieldConfigParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Sing
     * @Date 2020-12-11
     */
     PageInfo<FieldConfigResult> findPageBySpec(FieldConfigParam param);

}
