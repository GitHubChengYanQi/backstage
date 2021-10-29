package cn.atsoft.dasheng.orCode.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.orCode.entity.OrCode;
import cn.atsoft.dasheng.orCode.model.params.OrCodeParam;
import cn.atsoft.dasheng.orCode.model.result.OrCodeResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 二维码 服务类
 * </p>
 *
 * @author song
 * @since 2021-10-29
 */
public interface OrCodeService extends IService<OrCode> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-10-29
     */
    void add(OrCodeParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-10-29
     */
    void delete(OrCodeParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-10-29
     */
    void update(OrCodeParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-10-29
     */
    OrCodeResult findBySpec(OrCodeParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-10-29
     */
    List<OrCodeResult> findListBySpec(OrCodeParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-10-29
     */
     PageInfo<OrCodeResult> findPageBySpec(OrCodeParam param);

}
