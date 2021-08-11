package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.ItemClass;
import cn.atsoft.dasheng.app.model.params.ItemClassParam;
import cn.atsoft.dasheng.app.model.result.ItemClassResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 产品分类表 Mapper 接口
 * </p>
 *
 * @author cheng
 * @since 2021-08-11
 */
public interface ItemClassMapper extends BaseMapper<ItemClass> {

    /**
     * 获取列表
     *
     * @author cheng
     * @Date 2021-08-11
     */
    List<ItemClassResult> customList(@Param("paramCondition") ItemClassParam paramCondition);

    /**
     * 获取map列表
     *
     * @author cheng
     * @Date 2021-08-11
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ItemClassParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author cheng
     * @Date 2021-08-11
     */
    Page<ItemClassResult> customPageList(@Param("page") Page page, @Param("paramCondition") ItemClassParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author cheng
     * @Date 2021-08-11
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ItemClassParam paramCondition);

}
