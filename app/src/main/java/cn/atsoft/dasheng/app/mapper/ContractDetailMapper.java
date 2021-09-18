package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.ContractDetail;
import cn.atsoft.dasheng.app.model.params.ContractDetailParam;
import cn.atsoft.dasheng.app.model.result.ContractDetailResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 合同产品明细 Mapper 接口
 * </p>
 *
 * @author sb
 * @since 2021-09-18
 */
public interface ContractDetailMapper extends BaseMapper<ContractDetail> {

    /**
     * 获取列表
     *
     * @author sb
     * @Date 2021-09-18
     */
    List<ContractDetailResult> customList(@Param("paramCondition") ContractDetailParam paramCondition);

    /**
     * 获取map列表
     *
     * @author sb
     * @Date 2021-09-18
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ContractDetailParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author sb
     * @Date 2021-09-18
     */
    Page<ContractDetailResult> customPageList(@Param("page") Page page, @Param("paramCondition") ContractDetailParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author sb
     * @Date 2021-09-18
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ContractDetailParam paramCondition);

}
