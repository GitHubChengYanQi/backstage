package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.PartsList;
import cn.atsoft.dasheng.app.model.params.PartsListParam;
import cn.atsoft.dasheng.app.model.result.PartsListResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 零件表 服务类
 * </p>
 *
 * @author 
 * @since 2021-07-23
 */
public interface PartsListService extends IService<PartsList> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-07-23
     */
    void add(PartsListParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-07-23
     */
    void delete(PartsListParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-07-23
     */
    void update(PartsListParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-07-23
     */
    PartsListResult findBySpec(PartsListParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-07-23
     */
    List<PartsListResult> findListBySpec(PartsListParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-07-23
     */
     PageInfo<PartsListResult> findPageBySpec(PartsListParam param);

}
