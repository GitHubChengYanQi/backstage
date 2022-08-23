package cn.atsoft.dasheng.form.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.ActivitiSetpSetDetail;
import cn.atsoft.dasheng.form.model.params.ActivitiSetpSetDetailParam;
import cn.atsoft.dasheng.form.model.result.ActivitiSetpSetDetailResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 工序步骤详情表 服务类
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
public interface ActivitiSetpSetDetailService extends IService<ActivitiSetpSetDetail> {

    /**
     * 新增
     *
     * @author Sing
     * @Date 2021-11-10
     */
    void add(ActivitiSetpSetDetailParam param);

    /**
     * 删除
     *
     * @author Sing
     * @Date 2021-11-10
     */
    void delete(ActivitiSetpSetDetailParam param);

    /**
     * 更新
     *
     * @author Sing
     * @Date 2021-11-10
     */
    void update(ActivitiSetpSetDetailParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Sing
     * @Date 2021-11-10
     */
    ActivitiSetpSetDetailResult findBySpec(ActivitiSetpSetDetailParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Sing
     * @Date 2021-11-10
     */
    List<ActivitiSetpSetDetailResult> findListBySpec(ActivitiSetpSetDetailParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Sing
     * @Date 2021-11-10
     */
     PageInfo findPageBySpec(ActivitiSetpSetDetailParam param);

    List<ActivitiSetpSetDetailResult> getResultByStepsIds(List<Long> ids);
}
