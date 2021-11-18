package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.QualityTaskBind;
import cn.atsoft.dasheng.erp.model.params.QualityTaskBindParam;
import cn.atsoft.dasheng.erp.model.result.QualityTaskBindResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-11-17
 */
public interface QualityTaskBindMapper extends BaseMapper<QualityTaskBind> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-11-17
     */
    List<QualityTaskBindResult> customList(@Param("paramCondition") QualityTaskBindParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-11-17
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") QualityTaskBindParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-11-17
     */
    Page<QualityTaskBindResult> customPageList(@Param("page") Page page, @Param("paramCondition") QualityTaskBindParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-11-17
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") QualityTaskBindParam paramCondition);

}
