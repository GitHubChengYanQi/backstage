package cn.atsoft.dasheng.crm.service;

import cn.atsoft.dasheng.app.model.result.ContactsResult;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.ContactsBind;
import cn.atsoft.dasheng.crm.model.params.ContactsBindParam;
import cn.atsoft.dasheng.crm.model.result.ContactsBindResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 联系人绑定表 服务类
 * </p>
 *
 * @author song
 * @since 2021-09-22
 */
public interface ContactsBindService extends IService<ContactsBind> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-09-22
     */
    void add(ContactsBindParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-09-22
     */
    void delete(ContactsBindParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-09-22
     */
    void update(ContactsBindParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-09-22
     */
    ContactsBindResult findBySpec(ContactsBindParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-09-22
     */
    List<ContactsBindResult> findListBySpec(ContactsBindParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-09-22
     */
     PageInfo<ContactsBindResult> findPageBySpec(ContactsBindParam param);

    void ContractsFormat(List<ContactsResult> results, Long customerId);
}
