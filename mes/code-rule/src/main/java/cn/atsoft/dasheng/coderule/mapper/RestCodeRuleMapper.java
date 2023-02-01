package cn.atsoft.dasheng.coderule.mapper;

import cn.atsoft.dasheng.coderule.entity.RestCodeRule;
import cn.atsoft.dasheng.coderule.model.params.RestCodeRuleParam;
import cn.atsoft.dasheng.coderule.model.result.RestCodeRuleResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface RestCodeRuleMapper extends BaseMapper<RestCodeRule> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-10-22
     */
    List<RestCodeRuleResult> customList(@Param("paramCondition") RestCodeRuleParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-10-22
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") RestCodeRuleParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-10-22
     */
    Page<RestCodeRuleResult> customPageList(@Param("page") Page page, @Param("paramCondition") RestCodeRuleParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-10-22
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") RestCodeRuleParam paramCondition);

}
