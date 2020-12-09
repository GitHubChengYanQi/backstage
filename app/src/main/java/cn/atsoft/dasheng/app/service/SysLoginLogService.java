package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.SysLoginLog;
import cn.atsoft.dasheng.app.model.params.SysLoginLogParam;
import cn.atsoft.dasheng.app.model.result.SysLoginLogResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 登录记录 服务类
 * </p>
 *
 * @author sing
 * @since 2020-12-09
 */
public interface SysLoginLogService extends IService<SysLoginLog> {

    /**
     * 新增
     *
     * @author sing
     * @Date 2020-12-09
     */
    void add(SysLoginLogParam param);

    /**
     * 删除
     *
     * @author sing
     * @Date 2020-12-09
     */
    void delete(SysLoginLogParam param);

    /**
     * 更新
     *
     * @author sing
     * @Date 2020-12-09
     */
    void update(SysLoginLogParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author sing
     * @Date 2020-12-09
     */
    SysLoginLogResult findBySpec(SysLoginLogParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author sing
     * @Date 2020-12-09
     */
    List<SysLoginLogResult> findListBySpec(SysLoginLogParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author sing
     * @Date 2020-12-09
     */
     PageInfo findPageBySpec(SysLoginLogParam param);

}
