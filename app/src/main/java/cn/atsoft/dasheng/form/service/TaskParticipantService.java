package cn.atsoft.dasheng.form.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.TaskParticipant;
import cn.atsoft.dasheng.form.model.params.TaskParticipantParam;
import cn.atsoft.dasheng.form.model.result.TaskParticipantResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author song
 * @since 2022-08-16
 */
public interface TaskParticipantService extends IService<TaskParticipant> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-08-16
     */
    void add(TaskParticipantParam param);

    void addTaskPerson(Long taskId, List<Long> userIds);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-08-16
     */
    void delete(TaskParticipantParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-08-16
     */
    void update(TaskParticipantParam param);

    Boolean MasterDocumentPromoter(Long processId);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-08-16
     */
    TaskParticipantResult findBySpec(TaskParticipantParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-08-16
     */
    List<TaskParticipantResult> findListBySpec(TaskParticipantParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-08-16
     */
     PageInfo<TaskParticipantResult> findPageBySpec(TaskParticipantParam param);

}
