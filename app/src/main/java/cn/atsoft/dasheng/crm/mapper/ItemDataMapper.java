package cn.atsoft.dasheng.crm.mapper;

import cn.atsoft.dasheng.crm.entity.ItemData;
import cn.atsoft.dasheng.crm.model.params.ItemDataParam;
import cn.atsoft.dasheng.crm.model.result.ItemDataResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 产品资料 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-09-11
 */
public interface ItemDataMapper extends BaseMapper<ItemData> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-09-11
     */
    List<ItemDataResult> customList(@Param("paramCondition") ItemDataParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-09-11
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ItemDataParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-09-11
     */
    Page<ItemDataResult> customPageList(@Param("page") Page page, @Param("paramCondition") ItemDataParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-09-11
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ItemDataParam paramCondition);

}
