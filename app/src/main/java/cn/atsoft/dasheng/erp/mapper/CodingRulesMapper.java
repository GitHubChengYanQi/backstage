package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.CodingRules;
import cn.atsoft.dasheng.erp.model.params.CodingRulesParam;
import cn.atsoft.dasheng.erp.model.result.CodingRulesResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 编码规则 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-10-22
 */
public interface CodingRulesMapper extends BaseMapper<CodingRules> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-10-22
     */
    List<CodingRulesResult> customList(@Param("paramCondition") CodingRulesParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-10-22
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") CodingRulesParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-10-22
     */
    Page<CodingRulesResult> customPageList(@Param("page") Page page, @Param("paramCondition") CodingRulesParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-10-22
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") CodingRulesParam paramCondition);

}
