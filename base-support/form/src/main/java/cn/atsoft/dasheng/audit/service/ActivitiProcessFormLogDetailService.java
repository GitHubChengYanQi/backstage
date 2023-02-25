package cn.atsoft.dasheng.audit.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.audit.entity.ActivitiProcessLogDetail;
import cn.atsoft.dasheng.audit.model.params.ActivitiProcessLogDetailParam;
import cn.atsoft.dasheng.audit.model.result.ActivitiProcessLogDetailResult;
import cn.atsoft.dasheng.form.entity.ActivitiProcessLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 流程日志人员表 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-02-16
 */
public interface ActivitiProcessFormLogDetailService extends IService<ActivitiProcessLogDetail> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2023-02-16
     */
    void add(ActivitiProcessLogDetailParam param);

    void addByLog(ActivitiProcessLog log, List<Long> userIds);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2023-02-16
     */
    void delete(ActivitiProcessLogDetailParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2023-02-16
     */
    void update(ActivitiProcessLogDetailParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-02-16
     */
    ActivitiProcessLogDetailResult findBySpec(ActivitiProcessLogDetailParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-02-16
     */
    List<ActivitiProcessLogDetailResult> findListBySpec(ActivitiProcessLogDetailParam param);

    List<ActivitiProcessLogDetail> listByLogIds(List<Long> logIds);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-02-16
     */
     PageInfo<ActivitiProcessLogDetailResult> findPageBySpec(ActivitiProcessLogDetailParam param);

}
