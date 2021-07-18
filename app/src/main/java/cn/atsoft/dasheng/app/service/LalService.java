package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Lal;
import cn.atsoft.dasheng.app.model.params.LalParam;
import cn.atsoft.dasheng.app.model.result.LalResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 经纬度表 服务类
 * </p>
 *
 * @author 
 * @since 2021-07-16
 */
public interface LalService extends IService<Lal> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-07-16
     */
    void add(LalParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-07-16
     */
    void delete(LalParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-07-16
     */
    void update(LalParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-07-16
     */
    LalResult findBySpec(LalParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-07-16
     */
    List<LalResult> findListBySpec(LalParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-07-16
     */
     PageInfo<LalResult> findPageBySpec(LalParam param);

}
