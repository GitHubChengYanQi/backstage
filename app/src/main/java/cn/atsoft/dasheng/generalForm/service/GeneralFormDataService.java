package cn.atsoft.dasheng.generalForm.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.generalForm.entity.GeneralFormData;
import cn.atsoft.dasheng.generalForm.model.params.GeneralFormDataParam;
import cn.atsoft.dasheng.generalForm.model.result.GeneralFormDataResult;
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
public interface GeneralFormDataService extends IService<GeneralFormData> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-09-08
     */
    void add(GeneralFormDataParam param);

    void addBatch(List<GeneralFormDataParam> params);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-09-08
     */
    void delete(GeneralFormDataParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-09-08
     */
    void update(GeneralFormDataParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-09-08
     */
    GeneralFormDataResult findBySpec(GeneralFormDataParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-09-08
     */
    List<GeneralFormDataResult> findListBySpec(GeneralFormDataParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-09-08
     */
     PageInfo<GeneralFormDataResult> findPageBySpec(GeneralFormDataParam param);

}
