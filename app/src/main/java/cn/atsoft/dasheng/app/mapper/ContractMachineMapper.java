package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.ContractMachine;
import cn.atsoft.dasheng.app.model.params.ContractMachineParam;
import cn.atsoft.dasheng.app.model.result.ContractMachineResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 机床合同表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-07-20
 */
public interface ContractMachineMapper extends BaseMapper<ContractMachine> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-07-20
     */
    List<ContractMachineResult> customList(@Param("paramCondition") ContractMachineParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-07-20
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ContractMachineParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-07-20
     */
    Page<ContractMachineResult> customPageList(@Param("page") Page page, @Param("paramCondition") ContractMachineParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-07-20
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ContractMachineParam paramCondition);

}
