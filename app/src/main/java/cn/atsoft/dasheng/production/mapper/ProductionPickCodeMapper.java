package cn.atsoft.dasheng.production.mapper;

import cn.atsoft.dasheng.production.entity.ProductionPickCode;
import cn.atsoft.dasheng.production.model.params.ProductionPickCodeParam;
import cn.atsoft.dasheng.production.model.result.ProductionPickCodeResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 领取物料码 Mapper 接口
 * </p>
 *
 * @author cheng
 * @since 2022-03-29
 */
public interface ProductionPickCodeMapper extends BaseMapper<ProductionPickCode> {

    /**
     * 获取列表
     *
     * @author cheng
     * @Date 2022-03-29
     */
    List<ProductionPickCodeResult> customList(@Param("paramCondition") ProductionPickCodeParam paramCondition);

    /**
     * 获取map列表
     *
     * @author cheng
     * @Date 2022-03-29
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ProductionPickCodeParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author cheng
     * @Date 2022-03-29
     */
    Page<ProductionPickCodeResult> customPageList(@Param("page") Page page, @Param("paramCondition") ProductionPickCodeParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author cheng
     * @Date 2022-03-29
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ProductionPickCodeParam paramCondition);

}
