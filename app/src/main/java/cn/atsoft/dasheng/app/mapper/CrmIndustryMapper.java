package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.CrmIndustry;
import cn.atsoft.dasheng.app.model.params.CrmIndustryParam;
import cn.atsoft.dasheng.app.model.result.CrmIndustryResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 行业表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-08-02
 */
public interface CrmIndustryMapper extends BaseMapper<CrmIndustry> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-08-02
     */
    List<CrmIndustryResult> customList(@Param("paramCondition") CrmIndustryParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-08-02
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") CrmIndustryParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-08-02
     */
    Page<CrmIndustryResult> customPageList(@Param("page") Page page, @Param("paramCondition") CrmIndustryParam paramCondition,@Param("dataScope") DataScope dataScope);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-08-02
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") CrmIndustryParam paramCondition);

}
