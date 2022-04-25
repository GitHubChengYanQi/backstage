package cn.atsoft.dasheng.crm.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.DocumentsStatus;
import cn.atsoft.dasheng.crm.model.params.DocumentsStatusParam;
import cn.atsoft.dasheng.crm.model.result.DocumentsStatusResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 单据状态 服务类
 * </p>
 *
 * @author song
 * @since 2022-04-23
 */
public interface DocumentsStatusService extends IService<DocumentsStatus> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-04-23
     */
    void add(DocumentsStatusParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-04-23
     */
    void delete(DocumentsStatusParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-04-23
     */
    void update(DocumentsStatusParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-04-23
     */
    DocumentsStatusResult findBySpec(DocumentsStatusParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-04-23
     */
    List<DocumentsStatusResult> findListBySpec(DocumentsStatusParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-04-23
     */
     PageInfo<DocumentsStatusResult> findPageBySpec(DocumentsStatusParam param);

}
