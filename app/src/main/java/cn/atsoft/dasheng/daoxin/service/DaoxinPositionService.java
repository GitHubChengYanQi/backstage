package cn.atsoft.dasheng.daoxin.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.daoxin.entity.DaoxinPosition;
import cn.atsoft.dasheng.daoxin.model.params.DaoxinPositionParam;
import cn.atsoft.dasheng.daoxin.model.result.DaoxinPositionResult;
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
public interface DaoxinPositionService extends IService<DaoxinPosition> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
    void add(DaoxinPositionParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
    void delete(DaoxinPositionParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
    void update(DaoxinPositionParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
    DaoxinPositionResult findBySpec(DaoxinPositionParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
    List<DaoxinPositionResult> findListBySpec(DaoxinPositionParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
     PageInfo<DaoxinPositionResult> findPageBySpec(DaoxinPositionParam param);

}
