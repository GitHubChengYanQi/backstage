package cn.atsoft.dasheng.serial.mapper;

import cn.atsoft.dasheng.serial.entity.SerialNumber;
import cn.atsoft.dasheng.serial.model.params.SerialNumberParam;
import cn.atsoft.dasheng.serial.model.result.SerialNumberResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
public interface SerialNumberMapper extends BaseMapper<SerialNumber> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-11-04
     */
    List<SerialNumberResult> customList(@Param("paramCondition") SerialNumberParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-11-04
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") SerialNumberParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-11-04
     */
    Page<SerialNumberResult> customPageList(@Param("page") Page page, @Param("paramCondition") SerialNumberParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-11-04
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") SerialNumberParam paramCondition);

}
