package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.QualityIssuess;
import cn.atsoft.dasheng.erp.model.params.QualityIssuessParam;
import cn.atsoft.dasheng.erp.model.result.QualityIssuessResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 质检事项表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-11-15
 */
public interface QualityIssuessMapper extends BaseMapper<QualityIssuess> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-11-15
     */
    List<QualityIssuessResult> customList(@Param("paramCondition") QualityIssuessParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-11-15
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") QualityIssuessParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-11-15
     */
    Page<QualityIssuessResult> customPageList(@Param("page") Page page, @Param("paramCondition") QualityIssuessParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-11-15
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") QualityIssuessParam paramCondition);

}
