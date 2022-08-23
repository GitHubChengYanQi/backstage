package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Contacts;
import cn.atsoft.dasheng.app.model.params.ContactsParam;
import cn.atsoft.dasheng.app.model.result.ContactsResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 联系人表 服务类
 * </p>
 *
 * @author 
 * @since 2021-07-23
 */
public interface ContactsService extends IService<Contacts> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-07-23
     * @return
     */
    Contacts add(ContactsParam param);

    Long insert(ContactsParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-07-23
     * @return
     */
    Contacts delete(ContactsParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-07-23
     * @return
     */
    Contacts update(ContactsParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-07-23
     */
    ContactsResult findBySpec(ContactsParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-07-23
     */
    List<ContactsResult> findListBySpec(ContactsParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-07-23
     */
     PageInfo findPageBySpec(DataScope dataScope, ContactsParam param);

     void batchDelete(List<Long> id);

    void format(List<ContactsResult> result);

}
