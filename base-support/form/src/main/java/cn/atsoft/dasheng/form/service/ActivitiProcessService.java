package cn.atsoft.dasheng.form.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessParam;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessResult;
import cn.atsoft.dasheng.form.pojo.ProcessEnum;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 流程主表 服务类
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
public interface ActivitiProcessService extends IService<ActivitiProcess> {

    /**
     * 新增
     *
     * @author Sing
     * @Date 2021-11-10
     */
    void add(ActivitiProcessParam param);

    List<String> getType();

    /**
     * 删除
     *
     * @author Sing
     * @Date 2021-11-10
     */
    void delete(ActivitiProcessParam param);

    ActivitiProcess getByFromId(Long fromId);

    /**
     * 更新
     *
     * @author Sing
     * @Date 2021-11-10
     */
    void update(ActivitiProcessParam param);

    boolean judgePerson(String type, String module);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Sing
     * @Date 2021-11-10
     */
    ActivitiProcessResult findBySpec(ActivitiProcessParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Sing
     * @Date 2021-11-10
     */
    List<ActivitiProcessResult> findListBySpec(ActivitiProcessParam param);

    PageInfo<ActivitiProcessResult> findShipPageBySpec(ActivitiProcessParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Sing
     * @Date 2021-11-10
     */
     PageInfo<ActivitiProcessResult> findPageBySpec(ActivitiProcessParam param);

    List<String> getModule(ProcessEnum processEnum);
}
