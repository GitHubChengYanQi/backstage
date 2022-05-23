package cn.atsoft.dasheng.form.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.DocumentsPermissions;
import cn.atsoft.dasheng.form.model.params.DocumentsPermissionsParam;
import cn.atsoft.dasheng.form.model.result.DocumentsPermissionsResult;
import cn.atsoft.dasheng.form.pojo.PermissionParam;
import cn.atsoft.dasheng.form.pojo.RolePermission;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.transaction.Transactional;
import java.util.List;

/**
 * <p>
 * 单据权限 服务类
 * </p>
 *
 * @author 
 * @since 2022-05-18
 */
public interface DocumentsPermissionsService extends IService<DocumentsPermissions> {

    /**
     * 新增
     *
     * @author 
     * @Date 2022-05-18
     */
    void add(DocumentsPermissionsParam param);


    void addList(PermissionParam param);


    List<RolePermission> getRolePermission(String formType, String filedName);


    List<DocumentsPermissionsResult> getDetails(String formType);

    /**
     * 删除
     *
     * @author 
     * @Date 2022-05-18
     */
    void delete(DocumentsPermissionsParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2022-05-18
     */
    void update(DocumentsPermissionsParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2022-05-18
     */
    DocumentsPermissionsResult findBySpec(DocumentsPermissionsParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2022-05-18
     */
    List<DocumentsPermissionsResult> findListBySpec(DocumentsPermissionsParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2022-05-18
     */
     PageInfo<DocumentsPermissionsResult> findPageBySpec(DocumentsPermissionsParam param);

    List<DocumentsPermissionsResult> getAllPermissions();
}
