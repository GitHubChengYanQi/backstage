package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Contacts;
import cn.atsoft.dasheng.app.model.params.ContactsParam;
import cn.atsoft.dasheng.app.model.result.ContactsResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 联系人表 服务类
 * </p>
 *
 * @author ta
 * @since 2021-07-19
 */
public interface ContactsService extends IService<Contacts> {

    /**
     * 新增
     *
     * @author ta
     * @Date 2021-07-19
     */
    void add(ContactsParam param);

    /**
     * 删除
     *
     * @author ta
     * @Date 2021-07-19
     */
    void delete(ContactsParam param);

    /**
     * 更新
     *
     * @author ta
     * @Date 2021-07-19
     */
    void update(ContactsParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author ta
     * @Date 2021-07-19
     */
    ContactsResult findBySpec(ContactsParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author ta
     * @Date 2021-07-19
     */
    List<ContactsResult> findListBySpec(ContactsParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author ta
     * @Date 2021-07-19
     */
     PageInfo<ContactsResult> findPageBySpec(ContactsParam param);

}
