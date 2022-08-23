package cn.atsoft.dasheng.form.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.FormData;
import cn.atsoft.dasheng.form.model.params.FormDataParam;
import cn.atsoft.dasheng.form.model.result.FormDataResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 自定义表单主表 服务类
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
public interface FormDataService extends IService<FormData> {

    /**
     * 新增
     *
     * @author Sing
     * @Date 2021-11-10
     */
    void add(FormDataParam param);

    /**
     * 删除
     *
     * @author Sing
     * @Date 2021-11-10
     */
    void delete(FormDataParam param);

    /**
     * 更新
     *
     * @author Sing
     * @Date 2021-11-10
     */
    void update(FormDataParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Sing
     * @Date 2021-11-10
     */
    FormDataResult findBySpec(FormDataParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Sing
     * @Date 2021-11-10
     */
    List<FormDataResult> findListBySpec(FormDataParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Sing
     * @Date 2021-11-10
     */
    PageInfo findPageBySpec(FormDataParam param);

    /**
     * 查询dataId
     *
     * @param formId
     * @return
     */
    List<FormDataResult> getDataIds(List<Long> formId);

}
