package cn.atsoft.dasheng.purchase.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.purchase.entity.InquiryTask;
import cn.atsoft.dasheng.purchase.model.params.InquiryTaskParam;
import cn.atsoft.dasheng.purchase.model.result.InquiryTaskResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 询价任务 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-23
 */
public interface InquiryTaskService extends IService<InquiryTask> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
    void add(InquiryTaskParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
    void delete(InquiryTaskParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
    void update(InquiryTaskParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
    InquiryTaskResult findBySpec(InquiryTaskParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
    List<InquiryTaskResult> findListBySpec(InquiryTaskParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
     PageInfo<InquiryTaskResult> findPageBySpec(InquiryTaskParam param);

    InquiryTaskResult detail(Long taskId);

    void updateStatus(ActivitiProcessTask processTask);

    void updateRefuseStatus(ActivitiProcessTask processTask);
}
