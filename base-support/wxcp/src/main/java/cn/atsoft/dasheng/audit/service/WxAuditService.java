package cn.atsoft.dasheng.audit.service;

import cn.atsoft.dasheng.audit.entity.WxAudit;
import cn.atsoft.dasheng.audit.model.result.WxAuditPost;
import cn.atsoft.dasheng.audit.model.result.WxAuditResult;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.audit.model.params.WxAuditParam;
import com.baomidou.mybatisplus.extension.service.IService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.oa.WxCpOaApplyEventRequest;
import me.chanjar.weixin.cp.bean.oa.WxCpTemplateResult;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-03-18
 */
public interface WxAuditService extends IService<WxAudit> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2023-03-18
     */
    void add(WxAuditParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2023-03-18
     */
    void delete(WxAuditParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2023-03-18
     */
    void update(WxAuditParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-03-18
     */
    WxAuditResult findBySpec(WxAuditParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-03-18
     */
    List<WxAuditResult> findListBySpec(WxAuditParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-03-18
     */
     PageInfo<WxAuditResult> findPageBySpec(WxAuditParam param);

    WxAuditPost post(WxCpOaApplyEventRequest param) throws WxErrorException, IOException;

    WxCpTemplateResult getTemplate(String templateId) throws WxErrorException;
}
