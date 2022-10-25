package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.SkuHandleRecord;
import cn.atsoft.dasheng.erp.model.params.SkuHandleRecordParam;
import cn.atsoft.dasheng.erp.model.result.SkuHandleRecordResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * sku 任务操作记录 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2022-10-25
 */
public interface SkuHandleRecordMapper extends BaseMapper<SkuHandleRecord> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2022-10-25
     */
    List<SkuHandleRecordResult> customList(@Param("paramCondition") SkuHandleRecordParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2022-10-25
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") SkuHandleRecordParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2022-10-25
     */
    Page<SkuHandleRecordResult> customPageList(@Param("page") Page page, @Param("paramCondition") SkuHandleRecordParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2022-10-25
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") SkuHandleRecordParam paramCondition);

}
