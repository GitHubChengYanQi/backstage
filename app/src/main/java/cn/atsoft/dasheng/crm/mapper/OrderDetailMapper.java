package cn.atsoft.dasheng.crm.mapper;

import cn.atsoft.dasheng.crm.entity.OrderDetail;
import cn.atsoft.dasheng.crm.model.params.OrderDetailParam;
import cn.atsoft.dasheng.crm.model.result.OrderDetailResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2022-02-23
     */
    List<OrderDetailResult> customList(@Param("paramCondition") OrderDetailParam paramCondition);

    /**
     * 查询未加入生产计划的待生产任务
     * @param paramCondition
     * @return
     */
    List<OrderDetailResult> pendingProductionPlanByOrder(@Param("paramCondition") OrderDetailParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2022-02-23
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") OrderDetailParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2022-02-23
     */
    Page<OrderDetailResult> customPageList(@Param("page") Page page, @Param("paramCondition") OrderDetailParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2022-02-23
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") OrderDetailParam paramCondition);

}
