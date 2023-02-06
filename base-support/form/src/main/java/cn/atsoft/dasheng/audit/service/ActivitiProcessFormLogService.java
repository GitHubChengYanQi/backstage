package cn.atsoft.dasheng.audit.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.ActivitiProcessLog;
import cn.atsoft.dasheng.audit.model.params.ActivitiProcessLogParam;
import cn.atsoft.dasheng.audit.model.result.ActivitiProcessLogResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 流程日志表 服务类
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
public interface ActivitiProcessFormLogService extends IService<ActivitiProcessLog> {

    /**
     * 新增
     *
     * @author Sing
     * @Date 2021-11-10
     */
    void add(ActivitiProcessLogParam param);

    /**
     * 删除
     *
     * @author Sing
     * @Date 2021-11-10
     */
    void delete(ActivitiProcessLogParam param);

    /**
     * 更新
     *
     * @author Sing
     * @Date 2021-11-10
     */
    void update(ActivitiProcessLogParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Sing
     * @Date 2021-11-10
     */
    ActivitiProcessLogResult findBySpec(ActivitiProcessLogParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Sing
     * @Date 2021-11-10
     */
    List<ActivitiProcessLogResult> findListBySpec(ActivitiProcessLogParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Sing
     * @Date 2021-11-10
     */
    PageInfo findPageBySpec(ActivitiProcessLogParam param);




}
