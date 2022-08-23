package cn.atsoft.dasheng.form.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessTaskResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 流程任务表	 服务类
 * </p>
 *
 * @author Jazz
 * @since 2021-11-19
 */
public interface ActivitiProcessTaskService extends IService<ActivitiProcessTask> {

    /**
     * 新增
     *
     * @author Jazz
     * @Date 2021-11-19
     */
    void add(ActivitiProcessTaskParam param);

    /**
     * 删除
     *
     * @author Jazz
     * @Date 2021-11-19
     */
    void delete(ActivitiProcessTaskParam param);

    /**
     * 更新
     *
     * @author Jazz
     * @Date 2021-11-19
     */
    void update(ActivitiProcessTaskParam param);

    Long getTaskIdByFormId(Long formId);

    int isAdmin(Long taskId);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Jazz
     * @Date 2021-11-19
     */
    ActivitiProcessTaskResult findBySpec(ActivitiProcessTaskParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Jazz
     * @Date 2021-11-19
     */
    List<ActivitiProcessTaskResult> findListBySpec(ActivitiProcessTaskParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Jazz
     * @Date 2021-11-19
     */
    PageInfo findPageBySpec(ActivitiProcessTaskParam param);



}
