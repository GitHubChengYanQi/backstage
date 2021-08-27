package cn.atsoft.dasheng.portal.remindUser.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.remindUser.entity.RemindUser;
import cn.atsoft.dasheng.portal.remindUser.model.params.RemindUserParam;
import cn.atsoft.dasheng.portal.remindUser.model.result.RemindUserResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author c
 * @since 2021-08-27
 */
public interface RemindUserService extends IService<RemindUser> {

    /**
     * 新增
     *
     * @author c
     * @Date 2021-08-27
     */
    void add(RemindUserParam param);

    /**
     * 删除
     *
     * @author c
     * @Date 2021-08-27
     */
    void delete(RemindUserParam param);

    /**
     * 更新
     *
     * @author c
     * @Date 2021-08-27
     */
    void update(RemindUserParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author c
     * @Date 2021-08-27
     */
    RemindUserResult findBySpec(RemindUserParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author c
     * @Date 2021-08-27
     */
    List<RemindUserResult> findListBySpec(RemindUserParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author c
     * @Date 2021-08-27
     */
     PageInfo<RemindUserResult> findPageBySpec(RemindUserParam param);

}
