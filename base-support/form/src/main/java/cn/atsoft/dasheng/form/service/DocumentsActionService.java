package cn.atsoft.dasheng.form.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.DocumentsAction;
import cn.atsoft.dasheng.form.model.params.DocumentsActionParam;
import cn.atsoft.dasheng.form.model.result.DocumentsActionResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 单据动作 服务类
 * </p>
 *
 * @author song
 * @since 2022-04-28
 */
public interface DocumentsActionService extends IService<DocumentsAction> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-04-28
     */
    void add(DocumentsActionParam param);

    void removeAll(Long statusId);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-04-28
     */
    void delete(DocumentsActionParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-04-28
     */
    void update(DocumentsActionParam param);

    List<DocumentsActionResult> getList(Long statusId,String fromType);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-04-28
     */
    DocumentsActionResult findBySpec(DocumentsActionParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-04-28
     */
    List<DocumentsActionResult> findListBySpec(DocumentsActionParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-04-28
     */
     PageInfo<DocumentsActionResult> findPageBySpec(DocumentsActionParam param);

}
