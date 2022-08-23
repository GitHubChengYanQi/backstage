package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.app.model.result.TrackNumberResult;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.BusinessTrack;
import cn.atsoft.dasheng.app.model.params.BusinessTrackParam;
import cn.atsoft.dasheng.app.model.result.BusinessTrackResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 跟进内容 服务类
 * </p>
 *
 * @author cheng
 * @since 2021-09-17
 */
public interface BusinessTrackService extends IService<BusinessTrack> {

    /**
     * 新增
     *
     * @author cheng
     * @Date 2021-09-17
     */
    void add(BusinessTrackParam param);

    /**
     * 删除
     *
     * @author cheng
     * @Date 2021-09-17
     */
    void delete(BusinessTrackParam param);

    /**
     * 更新
     *
     * @author cheng
     * @Date 2021-09-17
     */
    void update(BusinessTrackParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author cheng
     * @Date 2021-09-17
     */
    BusinessTrackResult findBySpec(BusinessTrackParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author cheng
     * @Date 2021-09-17
     */
    List<BusinessTrackResult> findListBySpec(BusinessTrackParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author cheng
     * @Date 2021-09-17
     */
    PageInfo findPageBySpec(BusinessTrackParam param, DataScope dataScope);

    /**
     *查找分类数量
     * @return
     */
    TrackNumberResult findNumber();

}
