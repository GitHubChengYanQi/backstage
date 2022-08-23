package cn.atsoft.dasheng.form.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.FormDataValue;
import cn.atsoft.dasheng.form.model.params.FormDataValueParam;
import cn.atsoft.dasheng.form.model.result.FormDataValueResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 自定义表单各个字段数据	 服务类
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
public interface FormDataValueService extends IService<FormDataValue> {

    /**
     * 新增
     *
     * @author Sing
     * @Date 2021-11-10
     */
    void add(FormDataValueParam param);

    /**
     * 删除
     *
     * @author Sing
     * @Date 2021-11-10
     */
    void delete(FormDataValueParam param);

    /**
     * 更新
     *
     * @author Sing
     * @Date 2021-11-10
     */
    void update(FormDataValueParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Sing
     * @Date 2021-11-10
     */
    FormDataValueResult findBySpec(FormDataValueParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Sing
     * @Date 2021-11-10
     */
    List<FormDataValueResult> findListBySpec(FormDataValueParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Sing
     * @Date 2021-11-10
     */
    PageInfo findPageBySpec(FormDataValueParam param);

    /**
     * 多个dataId返回
     *
     * @param dataId
     * @return
     */
    List<FormDataValueResult> getDataValuesByDataId(List<Long> dataId);

    List<FormDataValueResult> getDataValuesResults (Long dataId);

}
