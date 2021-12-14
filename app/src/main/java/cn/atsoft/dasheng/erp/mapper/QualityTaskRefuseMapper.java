package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.QualityTaskRefuse;
import cn.atsoft.dasheng.erp.model.params.QualityTaskRefuseParam;
import cn.atsoft.dasheng.erp.model.result.QualityTaskRefuseResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 质检任务拒检 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-12-14
 */
public interface QualityTaskRefuseMapper extends BaseMapper<QualityTaskRefuse> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-12-14
     */
    List<QualityTaskRefuseResult> customList(@Param("paramCondition") QualityTaskRefuseParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-12-14
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") QualityTaskRefuseParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-12-14
     */
    Page<QualityTaskRefuseResult> customPageList(@Param("page") Page page, @Param("paramCondition") QualityTaskRefuseParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-12-14
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") QualityTaskRefuseParam paramCondition);

}
