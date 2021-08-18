package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.Items;
import cn.atsoft.dasheng.app.model.params.ItemsParam;
import cn.atsoft.dasheng.app.model.result.ItemsResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 物品表 Mapper 接口
 * </p>
 *
 * @author 1
 * @since 2021-07-14
 */
public interface ItemsMapper extends BaseMapper<Items> {

    /**
     * 获取列表
     *
     * @author 1
     * @Date 2021-07-14
     */
    List<ItemsResult> customList(@Param("paramCondition") ItemsParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 1
     * @Date 2021-07-14
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ItemsParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 1
     * @Date 2021-07-14
     */
    Page<ItemsResult> customPageList(@Param("page") Page page, @Param("paramCondition") ItemsParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 1
     * @Date 2021-07-14
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ItemsParam paramCondition);

}
