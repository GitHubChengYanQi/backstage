package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.Delivery;
import cn.atsoft.dasheng.app.model.params.DeliveryParam;
import cn.atsoft.dasheng.app.model.result.DeliveryResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 出库表 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-07-17
 */
public interface DeliveryMapper extends BaseMapper<Delivery> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-07-17
     */
    List<DeliveryResult> customList(@Param("paramCondition") DeliveryParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-07-17
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") DeliveryParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-07-17
     */
    Page<DeliveryResult> customPageList(@Param("page") Page page, @Param("paramCondition") DeliveryParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-07-17
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") DeliveryParam paramCondition);

}
