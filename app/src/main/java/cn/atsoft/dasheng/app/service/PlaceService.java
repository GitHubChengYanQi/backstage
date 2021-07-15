package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Place;
import cn.atsoft.dasheng.app.model.params.PlaceParam;
import cn.atsoft.dasheng.app.model.result.PlaceResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 地点表 服务类
 * </p>
 *
 * @author 
 * @since 2021-07-15
 */
public interface PlaceService extends IService<Place> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-07-15
     */
    void add(PlaceParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-07-15
     */
    void delete(PlaceParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-07-15
     */
    void update(PlaceParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-07-15
     */
    PlaceResult findBySpec(PlaceParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-07-15
     */
    List<PlaceResult> findListBySpec(PlaceParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-07-15
     */
     PageInfo<PlaceResult> findPageBySpec(PlaceParam param);

}
