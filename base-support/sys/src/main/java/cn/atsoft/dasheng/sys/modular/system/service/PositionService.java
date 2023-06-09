package cn.atsoft.dasheng.sys.modular.system.service;

import cn.atsoft.dasheng.base.pojo.page.LayuiPageInfo;
import cn.atsoft.dasheng.sys.modular.system.entity.Position;
import cn.atsoft.dasheng.sys.modular.system.model.params.PositionParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.PositionResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 职位表 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2019-06-27
 */
public interface PositionService extends IService<Position> {

    /**
     * 新增
     *
     * @author stylefeng
     * @Date 2019-06-27
     */
    Long add(PositionParam param);

    /**
     * 删除
     *
     * @author stylefeng
     * @Date 2019-06-27
     */
    void delete(PositionParam param);

    /**
     * 更新
     *
     * @author stylefeng
     * @Date 2019-06-27
     */
    void update(PositionParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author stylefeng
     * @Date 2019-06-27
     */
    PositionResult findBySpec(PositionParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author stylefeng
     * @Date 2019-06-27
     */
    List<PositionResult> findListBySpec(PositionParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author stylefeng
     * @Date 2019-06-27
     */
     LayuiPageInfo findPageBySpec(PositionParam param);

    /**
     * 获取多选框的职位列表
     *
     * @author stylefeng
     * @Date 2019-06-27
     */
    LayuiPageInfo listPositions(Long userId);

}
