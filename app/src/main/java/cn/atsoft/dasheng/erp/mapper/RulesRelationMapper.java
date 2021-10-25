package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.RulesRelation;
import cn.atsoft.dasheng.erp.model.params.RulesRelationParam;
import cn.atsoft.dasheng.erp.model.result.RulesRelationResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 编码规则和模块的对应关系 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-10-25
 */
public interface RulesRelationMapper extends BaseMapper<RulesRelation> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-10-25
     */
    List<RulesRelationResult> customList(@Param("paramCondition") RulesRelationParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-10-25
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") RulesRelationParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-10-25
     */
    Page<RulesRelationResult> customPageList(@Param("page") Page page, @Param("paramCondition") RulesRelationParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-10-25
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") RulesRelationParam paramCondition);

}
