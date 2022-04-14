package cn.atsoft.dasheng.auditView.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.auditView.entity.AuditView;
import cn.atsoft.dasheng.auditView.model.params.AuditViewParam;
import cn.atsoft.dasheng.auditView.model.result.AuditViewResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 所有审核 服务类
 * </p>
 *
 * @author song
 * @since 2021-12-16
 */
public interface AuditViewService extends IService<AuditView> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-12-16
     */
    void add(AuditViewParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-12-16
     */
    void delete(AuditViewParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-12-16
     */
    void update(AuditViewParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-12-16
     */
    AuditViewResult findBySpec(AuditViewParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-12-16
     */
    List<AuditViewResult> findListBySpec(AuditViewParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-12-16
     */
     PageInfo<AuditViewResult> findPageBySpec(AuditViewParam param);


    void addView(Long taskId);

    void microAddView(Long taskId, Long userId);
}
