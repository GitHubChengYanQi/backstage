package cn.atsoft.dasheng.crm.mapper;

import cn.atsoft.dasheng.crm.entity.InvoiceBill;
import cn.atsoft.dasheng.crm.model.params.InvoiceBillParam;
import cn.atsoft.dasheng.crm.model.result.InvoiceBillResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 发票 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2022-04-22
 */
public interface InvoiceBillMapper extends BaseMapper<InvoiceBill> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2022-04-22
     */
    List<InvoiceBillResult> customList(@Param("paramCondition") InvoiceBillParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2022-04-22
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") InvoiceBillParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2022-04-22
     */
    Page<InvoiceBillResult> customPageList(@Param("page") Page page, @Param("paramCondition") InvoiceBillParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2022-04-22
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") InvoiceBillParam paramCondition);

}
