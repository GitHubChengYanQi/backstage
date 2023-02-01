package cn.atsoft.dasheng.form.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.RestGeneralFormData;
import cn.atsoft.dasheng.form.model.params.RestGeneralFormDataParam;
import cn.atsoft.dasheng.form.model.result.RestGeneralFormDataResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-09-08
 */
public interface RestGeneralFormDataService extends IService<RestGeneralFormData> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-09-08
     */
    void add(RestGeneralFormDataParam param);

    void addBatch(List<RestGeneralFormDataParam> params);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-09-08
     */
    void delete(RestGeneralFormDataParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-09-08
     */
    void update(RestGeneralFormDataParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-09-08
     */
    RestGeneralFormDataResult findBySpec(RestGeneralFormDataParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-09-08
     */
    List<RestGeneralFormDataResult> findListBySpec(RestGeneralFormDataParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-09-08
     */
     PageInfo<RestGeneralFormDataResult> findPageBySpec(RestGeneralFormDataParam param);

}
