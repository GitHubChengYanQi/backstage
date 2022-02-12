package cn.atsoft.dasheng.production.mapper;

import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.crm.entity.Data;
import cn.atsoft.dasheng.production.entity.ShipSetp;
import cn.atsoft.dasheng.production.model.params.ShipSetpParam;
import cn.atsoft.dasheng.production.model.result.ShipSetpResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工序表 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2022-02-10
 */
public interface ShipSetpMapper extends BaseMapper<ShipSetp> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2022-02-10
     */
    List<ShipSetpResult> customList(@Param("paramCondition") ShipSetpParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2022-02-10
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ShipSetpParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2022-02-10
     */
    Page<ShipSetpResult> customPageList(@Param("page") Page page, @Param("paramCondition") ShipSetpParam paramCondition, @Param("dataScope")DataScope dataScope);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2022-02-10
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ShipSetpParam paramCondition);

}
