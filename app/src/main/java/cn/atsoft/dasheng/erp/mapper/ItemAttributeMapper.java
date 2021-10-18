package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.ItemAttribute;
import cn.atsoft.dasheng.erp.model.params.ItemAttributeParam;
import cn.atsoft.dasheng.erp.model.result.ItemAttributeResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 产品属性表 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-10-18
 */
public interface ItemAttributeMapper extends BaseMapper<ItemAttribute> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-10-18
     */
    List<ItemAttributeResult> customList(@Param("paramCondition") ItemAttributeParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-10-18
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ItemAttributeParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-10-18
     */
    Page<ItemAttributeResult> customPageList(@Param("page") Page page, @Param("paramCondition") ItemAttributeParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-10-18
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ItemAttributeParam paramCondition);

}
