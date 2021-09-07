package cn.atsoft.dasheng.crm.mapper;

import cn.atsoft.dasheng.crm.entity.BusinessCompetition;
import cn.atsoft.dasheng.crm.model.params.BusinessCompetitionParam;
import cn.atsoft.dasheng.crm.model.result.BusinessCompetitionResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商机 竞争对手 绑定 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-09-07
 */
public interface BusinessCompetitionMapper extends BaseMapper<BusinessCompetition> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-09-07
     */
    List<BusinessCompetitionResult> customList(@Param("paramCondition") BusinessCompetitionParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-09-07
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") BusinessCompetitionParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-09-07
     */
    Page<BusinessCompetitionResult> customPageList(@Param("page") Page page, @Param("paramCondition") BusinessCompetitionParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-09-07
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") BusinessCompetitionParam paramCondition);

}
