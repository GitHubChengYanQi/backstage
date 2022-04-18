package cn.atsoft.dasheng.crm.mapper;

import cn.atsoft.dasheng.crm.entity.ContractTempleteDetail;
import cn.atsoft.dasheng.crm.model.params.ContractTempleteDetailParam;
import cn.atsoft.dasheng.crm.model.result.ContractTempleteDetailResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 自定义合同变量 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-04-18
 */
public interface ContractTempleteDetailMapper extends BaseMapper<ContractTempleteDetail> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-04-18
     */
    List<ContractTempleteDetailResult> customList(@Param("paramCondition") ContractTempleteDetailParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-04-18
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ContractTempleteDetailParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-04-18
     */
    Page<ContractTempleteDetailResult> customPageList(@Param("page") Page page, @Param("paramCondition") ContractTempleteDetailParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-04-18
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ContractTempleteDetailParam paramCondition);

}
