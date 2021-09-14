package cn.atsoft.dasheng.crm.mapper;

import cn.atsoft.dasheng.crm.entity.Plan;
import cn.atsoft.dasheng.crm.model.params.PlanParam;
import cn.atsoft.dasheng.crm.model.result.PlanResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-09-14
 */
public interface PlanMapper extends BaseMapper<Plan> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-09-14
     */
    List<PlanResult> customList(@Param("paramCondition") PlanParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-09-14
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") PlanParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-09-14
     */
    Page<PlanResult> customPageList(@Param("page") Page page, @Param("paramCondition") PlanParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-09-14
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") PlanParam paramCondition);

}
