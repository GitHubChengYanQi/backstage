package cn.atsoft.dasheng.daoxin.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.daoxin.entity.Position;
import cn.atsoft.dasheng.daoxin.model.params.PositionParam;
import cn.atsoft.dasheng.daoxin.model.result.PositionResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * daoxin职位表 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-02-17
 */
public interface PositionService extends IService<Position> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
    void add(PositionParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
    void delete(PositionParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
    void update(PositionParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
    PositionResult findBySpec(PositionParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
    List<PositionResult> findListBySpec(PositionParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
     PageInfo<PositionResult> findPageBySpec(PositionParam param);

}
