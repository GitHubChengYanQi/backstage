package cn.atsoft.dasheng.crm.mapper;

import cn.atsoft.dasheng.crm.entity.DataClassification;
import cn.atsoft.dasheng.crm.model.params.DataClassificationParam;
import cn.atsoft.dasheng.crm.model.result.DataClassificationResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 资料分类表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-09-13
 */
public interface DataClassificationMapper extends BaseMapper<DataClassification> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-09-13
     */
    List<DataClassificationResult> customList(@Param("paramCondition") DataClassificationParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-09-13
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") DataClassificationParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-09-13
     */
    Page<DataClassificationResult> customPageList(@Param("page") Page page, @Param("paramCondition") DataClassificationParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-09-13
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") DataClassificationParam paramCondition);

}
