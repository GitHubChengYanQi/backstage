package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.SysUser;
import cn.atsoft.dasheng.app.model.params.SysUserParam;
import cn.atsoft.dasheng.app.model.result.SysUserResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 管理员表 服务类
 * </p>
 *
 * @author 
 * @since 2021-08-06
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-08-06
     */
    void add(SysUserParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-08-06
     */
    void delete(SysUserParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-08-06
     */
    void update(SysUserParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-08-06
     */
    SysUserResult findBySpec(SysUserParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-08-06
     */
    List<SysUserResult> findListBySpec(SysUserParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-08-06
     */
     PageInfo<SysUserResult> findPageBySpec(SysUserParam param);

}
