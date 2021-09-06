package cn.atsoft.dasheng.crm.mapper;

import cn.atsoft.dasheng.crm.entity.Competitor;
import cn.atsoft.dasheng.crm.model.params.CompetitorParam;
import cn.atsoft.dasheng.crm.model.result.CompetitorResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 竞争对手管理 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-09-06
 */
public interface CompetitorMapper extends BaseMapper<Competitor> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-09-06
     */
    List<CompetitorResult> customList(@Param("paramCondition") CompetitorParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-09-06
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") CompetitorParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-09-06
     */
    Page<CompetitorResult> customPageList(@Param("page") Page page, @Param("paramCondition") CompetitorParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-09-06
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") CompetitorParam paramCondition);

}
