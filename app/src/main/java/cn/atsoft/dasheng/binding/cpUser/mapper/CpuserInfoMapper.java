package cn.atsoft.dasheng.binding.cpUser.mapper;

import cn.atsoft.dasheng.binding.cpUser.entity.CpuserInfo;
import cn.atsoft.dasheng.binding.cpUser.model.params.CpuserInfoParam;
import cn.atsoft.dasheng.binding.cpUser.model.result.CpuserInfoResult;
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
 * @since 2021-10-12
 */
public interface CpuserInfoMapper extends BaseMapper<CpuserInfo> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-10-12
     */
    List<CpuserInfoResult> customList(@Param("paramCondition") CpuserInfoParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-10-12
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") CpuserInfoParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-10-12
     */
    Page<CpuserInfoResult> customPageList(@Param("page") Page page, @Param("paramCondition") CpuserInfoParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-10-12
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") CpuserInfoParam paramCondition);

}
