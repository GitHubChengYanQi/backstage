package cn.atsoft.dasheng.common_area.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.common_area.entity.CommonArea;
import cn.atsoft.dasheng.common_area.model.params.CommonAreaParam;
import cn.atsoft.dasheng.common_area.model.result.CommonAreaResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 逐渐取代region表 服务类
 * </p>
 *
 * @author
 * @since 2021-08-24
 */
public interface CommonAreaService extends IService<CommonArea> {

    /**
     * 新增
     *
     * @author
     * @Date 2021-08-24
     */
    void add(CommonAreaParam param);

    /**
     * 删除
     *
     * @author
     * @Date 2021-08-24
     */
    void delete(CommonAreaParam param);

    /**
     * 更新
     *
     * @author
     * @Date 2021-08-24
     */
    void update(CommonAreaParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author
     * @Date 2021-08-24
     */
    CommonAreaResult findBySpec(CommonAreaParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author
     * @Date 2021-08-24
     */
    List<CommonAreaResult> findListBySpec(CommonAreaParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author
     * @Date 2021-08-24
     */
    PageInfo<CommonAreaResult> findPageBySpec(CommonAreaParam param);

    List<CommonAreaResult> getProvince();


    List<CommonAreaResult> getCity(CommonAreaParam param);

    List<CommonAreaResult> getArea(CommonAreaParam param);
}
