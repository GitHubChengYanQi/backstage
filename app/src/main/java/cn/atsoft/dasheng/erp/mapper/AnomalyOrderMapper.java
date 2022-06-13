package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.AnomalyOrder;
import cn.atsoft.dasheng.erp.model.params.AnomalyOrderParam;
import cn.atsoft.dasheng.erp.model.result.AnomalyOrderResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author song
 * @since 2022-06-09
 */
public interface AnomalyOrderMapper extends BaseMapper<AnomalyOrder> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2022-06-09
     */
    List<AnomalyOrderResult> customList(@Param("paramCondition") AnomalyOrderParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2022-06-09
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") AnomalyOrderParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2022-06-09
     */
    Page<AnomalyOrderResult> customPageList(@Param("page") Page page, @Param("paramCondition") AnomalyOrderParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2022-06-09
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") AnomalyOrderParam paramCondition);

}
