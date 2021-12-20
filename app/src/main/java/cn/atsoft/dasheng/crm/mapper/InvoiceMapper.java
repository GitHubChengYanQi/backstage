package cn.atsoft.dasheng.crm.mapper;

import cn.atsoft.dasheng.crm.entity.Invoice;
import cn.atsoft.dasheng.crm.model.params.InvoiceParam;
import cn.atsoft.dasheng.crm.model.result.InvoiceResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 供应商开票 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-12-20
 */
public interface InvoiceMapper extends BaseMapper<Invoice> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-12-20
     */
    List<InvoiceResult> customList(@Param("paramCondition") InvoiceParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-12-20
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") InvoiceParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-12-20
     */
    Page<InvoiceResult> customPageList(@Param("page") Page page, @Param("paramCondition") InvoiceParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-12-20
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") InvoiceParam paramCondition);

}
