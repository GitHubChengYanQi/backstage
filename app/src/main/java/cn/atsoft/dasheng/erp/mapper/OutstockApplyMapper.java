package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.OutstockApply;
import cn.atsoft.dasheng.erp.model.params.OutstockApplyParam;
import cn.atsoft.dasheng.erp.model.result.OutstockApplyResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 出库申请 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-09-14
 */
public interface OutstockApplyMapper extends BaseMapper<OutstockApply> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-09-14
     */
    List<OutstockApplyResult> customList(@Param("paramCondition") OutstockApplyParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-09-14
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") OutstockApplyParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-09-14
     */
    Page<OutstockApplyResult> customPageList(@Param("page") Page page, @Param("paramCondition") OutstockApplyParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-09-14
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") OutstockApplyParam paramCondition);

}
