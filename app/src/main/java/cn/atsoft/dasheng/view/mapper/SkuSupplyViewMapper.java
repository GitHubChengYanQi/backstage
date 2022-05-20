package cn.atsoft.dasheng.view.mapper;

import cn.atsoft.dasheng.view.entity.SkuSupplyView;
import cn.atsoft.dasheng.view.model.params.SkuSupplyViewParam;
import cn.atsoft.dasheng.view.model.result.SkuSupplyViewResult;
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
public interface SkuSupplyViewMapper extends BaseMapper<SkuSupplyView> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2022-05-20
     */
    List<SkuSupplyViewResult> customList(@Param("paramCondition") SkuSupplyViewParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2022-05-20
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") SkuSupplyViewParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2022-05-20
     */
    Page<SkuSupplyViewResult> customPageList(@Param("page") Page page, @Param("paramCondition") SkuSupplyViewParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2022-05-20
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") SkuSupplyViewParam paramCondition);

}
