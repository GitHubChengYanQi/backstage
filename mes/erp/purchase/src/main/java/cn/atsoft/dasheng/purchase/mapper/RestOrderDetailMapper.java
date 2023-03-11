package cn.atsoft.dasheng.purchase.mapper;

import cn.atsoft.dasheng.purchase.entity.RestOrderDetail;
import cn.atsoft.dasheng.purchase.model.params.RestOrderDetailParam;
import cn.atsoft.dasheng.purchase.model.params.ViewParam;
import cn.atsoft.dasheng.purchase.model.result.RestOrderDetailResult;
import cn.atsoft.dasheng.purchase.model.result.ViewResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单明细表 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2022-02-23
 */
public interface RestOrderDetailMapper extends BaseMapper<RestOrderDetail> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2022-02-23
     */
    List<RestOrderDetailResult> customList(@Param("paramCondition") RestOrderDetailParam paramCondition);

    /**
     * 查询未加入生产计划的待生产任务
     * @param paramCondition
     * @return
     */
    List<RestOrderDetailResult> pendingProductionPlanByOrder(@Param("paramCondition") RestOrderDetailParam paramCondition);
    List<ViewResult> view(@Param("paramCondition") ViewParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2022-02-23
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") RestOrderDetailParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2022-02-23
     */
    Page<RestOrderDetailResult> customPageList(@Param("page") Page page, @Param("paramCondition") RestOrderDetailParam paramCondition);
    List<RestOrderDetailResult> historyList(@Param("paramCondition") RestOrderDetailParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2022-02-23
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") RestOrderDetailParam paramCondition);

}
