package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.DeliveryDetails;
import cn.atsoft.dasheng.app.model.params.DeliveryDetailsParam;
import cn.atsoft.dasheng.app.model.result.DeliveryDetailsResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
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
 * @author  
 * @since 2021-08-20
 */
public interface DeliveryDetailsMapper extends BaseMapper<DeliveryDetails> {

    /**
     * 获取列表
     *
     * @author  
     * @Date 2021-08-20
     */
    List<DeliveryDetailsResult> customList(@Param("paramCondition") DeliveryDetailsParam paramCondition);

    /**
     * 获取map列表
     *
     * @author  
     * @Date 2021-08-20
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") DeliveryDetailsParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author  
     * @Date 2021-08-20
     */
    Page<DeliveryDetailsResult> customPageList(@Param("page") Page page, @Param("paramCondition") DeliveryDetailsParam paramCondition,@Param("dataScope") DataScope dataScope);

    /**
     * 获取分页map列表
     *
     * @author  
     * @Date 2021-08-20
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") DeliveryDetailsParam paramCondition);

}
