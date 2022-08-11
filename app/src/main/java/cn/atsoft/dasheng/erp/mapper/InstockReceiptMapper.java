package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.InstockReceipt;
import cn.atsoft.dasheng.erp.model.params.InstockReceiptParam;
import cn.atsoft.dasheng.erp.model.result.InstockReceiptResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 入库记录单 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2022-08-11
 */
public interface InstockReceiptMapper extends BaseMapper<InstockReceipt> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2022-08-11
     */
    List<InstockReceiptResult> customList(@Param("paramCondition") InstockReceiptParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2022-08-11
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") InstockReceiptParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2022-08-11
     */
    Page<InstockReceiptResult> customPageList(@Param("page") Page page, @Param("paramCondition") InstockReceiptParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2022-08-11
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") InstockReceiptParam paramCondition);

}
