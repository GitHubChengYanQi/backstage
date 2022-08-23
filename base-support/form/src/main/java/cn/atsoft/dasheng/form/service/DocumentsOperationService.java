package cn.atsoft.dasheng.form.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.DocumentsOperation;
import cn.atsoft.dasheng.form.model.params.DocumentsOperationParam;
import cn.atsoft.dasheng.form.model.result.DocumentsOperationResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 单据权限操作 服务类
 * </p>
 *
 * @author 
 * @since 2022-05-18
 */
public interface DocumentsOperationService extends IService<DocumentsOperation> {

    /**
     * 新增
     *
     * @author 
     * @Date 2022-05-18
     */
    void add(DocumentsOperationParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2022-05-18
     */
    void delete(DocumentsOperationParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2022-05-18
     */
    void update(DocumentsOperationParam param);



    List<DocumentsOperationResult> getResultsByPermissionId(List<Long> ids);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2022-05-18
     */
    DocumentsOperationResult findBySpec(DocumentsOperationParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2022-05-18
     */
    List<DocumentsOperationResult> findListBySpec(DocumentsOperationParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2022-05-18
     */
     PageInfo findPageBySpec(DocumentsOperationParam param);

    DocumentsOperation getEntity(DocumentsOperationParam param);
}
