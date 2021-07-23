package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.Contract;
import cn.atsoft.dasheng.app.model.params.ContractParam;
import cn.atsoft.dasheng.app.model.result.ContractResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 合同表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-07-21
 */
public interface ContractMapper extends BaseMapper<Contract> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-07-21
     */
    List<ContractResult> customList(@Param("paramCondition") ContractParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-07-21
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ContractParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-07-21
     */
    Page<ContractResult> customPageList(@Param("page") Page page, @Param("paramCondition") ContractParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-07-21
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ContractParam paramCondition);

}
