package cn.atsoft.dasheng.view.mapper;

import cn.atsoft.dasheng.view.entity.SkuBasisView;
import cn.atsoft.dasheng.view.model.params.SkuBasisViewParam;
import cn.atsoft.dasheng.view.model.result.SkuBasisViewResult;
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
public interface SkuBasisViewMapper extends BaseMapper<SkuBasisView> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2022-05-20
     */
    List<SkuBasisViewResult> customList(@Param("paramCondition") SkuBasisViewParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2022-05-20
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") SkuBasisViewParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2022-05-20
     */
    Page<SkuBasisViewResult> customPageList(@Param("page") Page page, @Param("paramCondition") SkuBasisViewParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2022-05-20
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") SkuBasisViewParam paramCondition);

}
