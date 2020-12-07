package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Info;
import cn.atsoft.dasheng.app.model.params.InfoParam;
import cn.atsoft.dasheng.app.model.result.InfoResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 数据库信息表 服务类
 * </p>
 *
 * @author sing
 * @since 2020-12-07
 */
public interface InfoService extends IService<Info> {

    /**
     * 新增
     *
     * @author sing
     * @Date 2020-12-07
     */
    void add(InfoParam param);

    /**
     * 删除
     *
     * @author sing
     * @Date 2020-12-07
     */
    void delete(InfoParam param);

    /**
     * 更新
     *
     * @author sing
     * @Date 2020-12-07
     */
    void update(InfoParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author sing
     * @Date 2020-12-07
     */
    InfoResult findBySpec(InfoParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author sing
     * @Date 2020-12-07
     */
    List<InfoResult> findListBySpec(InfoParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author sing
     * @Date 2020-12-07
     */
     PageInfo findPageBySpec(InfoParam param);

}
