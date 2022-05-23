package cn.atsoft.dasheng.view.mapper;

import cn.atsoft.dasheng.view.entity.SkuPositionView;
import cn.atsoft.dasheng.view.model.params.SkuPositionViewParam;
import cn.atsoft.dasheng.view.model.result.SkuPositionViewResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * VIEW Mapper 接口
 * </p>
 *
 * @author 
 * @since 2022-05-20
 */
public interface SkuPositionViewMapper extends BaseMapper<SkuPositionView> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2022-05-20
     */
    List<SkuPositionViewResult> customList(@Param("paramCondition") SkuPositionViewParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2022-05-20
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") SkuPositionViewParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2022-05-20
     */
    Page<SkuPositionViewResult> customPageList(@Param("page") Page page, @Param("paramCondition") SkuPositionViewParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2022-05-20
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") SkuPositionViewParam paramCondition);

}
