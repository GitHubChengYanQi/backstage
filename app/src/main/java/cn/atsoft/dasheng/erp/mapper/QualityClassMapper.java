package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.QualityClass;
import cn.atsoft.dasheng.erp.model.params.QualityClassParam;
import cn.atsoft.dasheng.erp.model.result.QualityClassResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 质检方案详情分类 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-10-28
 */
public interface QualityClassMapper extends BaseMapper<QualityClass> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-10-28
     */
    List<QualityClassResult> customList(@Param("paramCondition") QualityClassParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-10-28
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") QualityClassParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-10-28
     */
    Page<QualityClassResult> customPageList(@Param("page") Page page, @Param("paramCondition") QualityClassParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-10-28
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") QualityClassParam paramCondition);

}
