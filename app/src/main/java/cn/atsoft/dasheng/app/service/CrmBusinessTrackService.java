package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.CrmBusinessTrack;
import cn.atsoft.dasheng.app.model.params.CrmBusinessTrackParam;
import cn.atsoft.dasheng.app.model.result.CrmBusinessTrackResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商机跟踪表 服务类
 * </p>
 *
 * @author 
 * @since 2021-08-04
 */
public interface CrmBusinessTrackService extends IService<CrmBusinessTrack> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-08-04
     */
    void add(CrmBusinessTrackParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-08-04
     */
    void delete(CrmBusinessTrackParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-08-04
     */
    void update(CrmBusinessTrackParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-08-04
     */
    CrmBusinessTrackResult findBySpec(CrmBusinessTrackParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-08-04
     */
    List<CrmBusinessTrackResult> findListBySpec(CrmBusinessTrackParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-08-04
     */
     PageInfo<CrmBusinessTrackResult> findPageBySpec(CrmBusinessTrackParam param);

}
