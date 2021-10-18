package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.AttributeValues;
import cn.atsoft.dasheng.erp.model.params.AttributeValuesParam;
import cn.atsoft.dasheng.erp.model.result.AttributeValuesResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 产品属性数据表 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-10-18
 */
public interface AttributeValuesMapper extends BaseMapper<AttributeValues> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-10-18
     */
    List<AttributeValuesResult> customList(@Param("paramCondition") AttributeValuesParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-10-18
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") AttributeValuesParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-10-18
     */
    Page<AttributeValuesResult> customPageList(@Param("page") Page page, @Param("paramCondition") AttributeValuesParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-10-18
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") AttributeValuesParam paramCondition);

}
