package cn.atsoft.dasheng.form.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.FormFieldConfig;
import cn.atsoft.dasheng.form.model.params.FormFieldConfigParam;
import cn.atsoft.dasheng.form.model.result.FormFieldConfigResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 自定义表单字段设置 服务类
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
public interface FormFieldConfigService extends IService<FormFieldConfig> {

    /**
     * 新增
     *
     * @author Sing
     * @Date 2021-11-10
     */
    void add(FormFieldConfigParam param);

    /**
     * 删除
     *
     * @author Sing
     * @Date 2021-11-10
     */
    void delete(FormFieldConfigParam param);

    /**
     * 更新
     *
     * @author Sing
     * @Date 2021-11-10
     */
    void update(FormFieldConfigParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Sing
     * @Date 2021-11-10
     */
    FormFieldConfigResult findBySpec(FormFieldConfigParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Sing
     * @Date 2021-11-10
     */
    List<FormFieldConfigResult> findListBySpec(FormFieldConfigParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Sing
     * @Date 2021-11-10
     */
     PageInfo findPageBySpec(FormFieldConfigParam param);

}
