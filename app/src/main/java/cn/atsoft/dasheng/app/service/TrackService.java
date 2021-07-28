package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Track;
import cn.atsoft.dasheng.app.model.params.TrackParam;
import cn.atsoft.dasheng.app.model.result.TrackResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 报价表 服务类
 * </p>
 *
 * @author cheng
 * @since 2021-07-19
 */
public interface TrackService extends IService<Track> {

    /**
     * 新增
     *
     * @author cheng
     * @Date 2021-07-19
     */
    Long add(TrackParam param);

    /**
     * 删除
     *
     * @author cheng
     * @Date 2021-07-19
     */
    void delete(TrackParam param);

    /**
     * 更新
     *
     * @author cheng
     * @Date 2021-07-19
     */
    void update(TrackParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author cheng
     * @Date 2021-07-19
     */
    TrackResult findBySpec(TrackParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author cheng
     * @Date 2021-07-19
     */
    List<TrackResult> findListBySpec(TrackParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author cheng
     * @Date 2021-07-19
     */
     PageInfo<TrackResult> findPageBySpec(TrackParam param);

}
