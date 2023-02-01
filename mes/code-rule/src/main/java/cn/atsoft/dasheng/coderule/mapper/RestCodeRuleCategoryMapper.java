package cn.atsoft.dasheng.coderule.mapper;

import cn.atsoft.dasheng.coderule.entity.RestCodeRuleCategory;
import cn.atsoft.dasheng.coderule.model.params.RestCodeRuleCategoryParam;
import cn.atsoft.dasheng.coderule.model.result.RestCodeRuleCategoryResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 编码规则分类 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-10-22
 */
public interface RestCodeRuleCategoryMapper extends BaseMapper<RestCodeRuleCategory> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-10-22
     */
    List<RestCodeRuleCategoryResult> customList(@Param("paramCondition") RestCodeRuleCategoryParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-10-22
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") RestCodeRuleCategoryParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-10-22
     */
    Page<RestCodeRuleCategoryResult> customPageList(@Param("page") Page page, @Param("paramCondition") RestCodeRuleCategoryParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-10-22
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") RestCodeRuleCategoryParam paramCondition);

}
