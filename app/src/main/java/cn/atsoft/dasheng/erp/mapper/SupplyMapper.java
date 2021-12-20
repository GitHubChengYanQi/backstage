package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.Supply;
import cn.atsoft.dasheng.erp.model.params.SupplyParam;
import cn.atsoft.dasheng.erp.model.result.SupplyResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 供应商供应物料 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-12-20
 */
public interface SupplyMapper extends BaseMapper<Supply> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-12-20
     */
    List<SupplyResult> customList(@Param("paramCondition") SupplyParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-12-20
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") SupplyParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-12-20
     */
    Page<SupplyResult> customPageList(@Param("page") Page page, @Param("paramCondition") SupplyParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-12-20
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") SupplyParam paramCondition);

}
