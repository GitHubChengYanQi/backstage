package cn.atsoft.dasheng.crm.mapper;

import cn.atsoft.dasheng.crm.entity.ContractClass;
import cn.atsoft.dasheng.crm.model.params.ContractClassParam;
import cn.atsoft.dasheng.crm.model.result.ContractClassResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 合同分类 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-12-09
 */
public interface ContractClassMapper extends BaseMapper<ContractClass> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-12-09
     */
    List<ContractClassResult> customList(@Param("paramCondition") ContractClassParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-12-09
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ContractClassParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-12-09
     */
    Page<ContractClassResult> customPageList(@Param("page") Page page, @Param("paramCondition") ContractClassParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-12-09
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ContractClassParam paramCondition);

}
