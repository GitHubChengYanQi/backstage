package cn.atsoft.dasheng.coderule.mapper;

import cn.atsoft.dasheng.coderule.entity.RestSerialNumber;
import cn.atsoft.dasheng.coderule.model.params.RestSerialNumberParam;
import cn.atsoft.dasheng.coderule.model.result.RestSerialNumberResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 流水号 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-11-04
 */
public interface RestSerialNumberMapper extends BaseMapper<RestSerialNumber> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-11-04
     */
    List<RestSerialNumberResult> customList(@Param("paramCondition") RestSerialNumberParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-11-04
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") RestSerialNumberParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-11-04
     */
    Page<RestSerialNumberResult> customPageList(@Param("page") Page page, @Param("paramCondition") RestSerialNumberParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-11-04
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") RestSerialNumberParam paramCondition);

}
