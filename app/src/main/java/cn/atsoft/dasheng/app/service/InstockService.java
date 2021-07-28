package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Instock;
import cn.atsoft.dasheng.app.model.params.InstockParam;
import cn.atsoft.dasheng.app.model.result.InstockResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 入库表 服务类
 * </p>
 *
 * @author song
 * @since 2021-07-17
 */
public interface InstockService extends IService<Instock> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-07-17
     */
    Long add(InstockParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-07-17
     */
    void delete(InstockParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-07-17
     */
    void update(InstockParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-07-17
     */
    InstockResult findBySpec(InstockParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-07-17
     */
    List<InstockResult> findListBySpec(InstockParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-07-17
     */
     PageInfo<InstockResult> findPageBySpec(InstockParam param);

}
