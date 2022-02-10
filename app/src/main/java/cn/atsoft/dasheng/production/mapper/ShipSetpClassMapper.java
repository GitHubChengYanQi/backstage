package cn.atsoft.dasheng.production.mapper;

import cn.atsoft.dasheng.production.entity.ShipSetpClass;
import cn.atsoft.dasheng.production.model.params.ShipSetpClassParam;
import cn.atsoft.dasheng.production.model.result.ShipSetpClassResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工序分类表 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2022-02-10
 */
public interface ShipSetpClassMapper extends BaseMapper<ShipSetpClass> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2022-02-10
     */
    List<ShipSetpClassResult> customList(@Param("paramCondition") ShipSetpClassParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2022-02-10
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ShipSetpClassParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2022-02-10
     */
    Page<ShipSetpClassResult> customPageList(@Param("page") Page page, @Param("paramCondition") ShipSetpClassParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2022-02-10
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ShipSetpClassParam paramCondition);

}
