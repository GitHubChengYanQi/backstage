package cn.atsoft.dasheng.crm.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.TrackMessage;
import cn.atsoft.dasheng.crm.model.params.TrackMessageParam;
import cn.atsoft.dasheng.crm.model.result.TrackMessageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商机跟踪内容 服务类
 * </p>
 *
 * @author 
 * @since 2021-09-07
 */
public interface TrackMessageService extends IService<TrackMessage> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-09-07
     * @return
     */
    TrackMessage add(TrackMessageParam param);


    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-09-07
     */
    TrackMessageResult findBySpec(TrackMessageParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-09-07
     */
    List<TrackMessageResult> findListBySpec(TrackMessageParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-09-07
     */
     PageInfo<TrackMessageResult> findPageBySpec(TrackMessageParam param);

}
