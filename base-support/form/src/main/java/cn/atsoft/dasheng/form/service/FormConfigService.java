package cn.atsoft.dasheng.form.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.FormConfig;
import cn.atsoft.dasheng.form.model.params.FormConfigParam;
import cn.atsoft.dasheng.form.model.result.FormConfigResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 自定义表单 服务类
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
public interface FormConfigService extends IService<FormConfig> {

    /**
     * 新增
     *
     * @author Sing
     * @Date 2021-11-10
     */
    void add(FormConfigParam param);

    /**
     * 删除
     *
     * @author Sing
     * @Date 2021-11-10
     */
    void delete(FormConfigParam param);

    /**
     * 更新
     *
     * @author Sing
     * @Date 2021-11-10
     */
    void update(FormConfigParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Sing
     * @Date 2021-11-10
     */
    FormConfigResult findBySpec(FormConfigParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Sing
     * @Date 2021-11-10
     */
    List<FormConfigResult> findListBySpec(FormConfigParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Sing
     * @Date 2021-11-10
     */
     PageInfo findPageBySpec(FormConfigParam param);

}
