package cn.atsoft.dasheng.purchase.mapper;

import cn.atsoft.dasheng.purchase.entity.InquiryTask;
import cn.atsoft.dasheng.purchase.model.params.InquiryTaskParam;
import cn.atsoft.dasheng.purchase.model.result.InquiryTaskResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 询价任务 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-23
 */
public interface InquiryTaskMapper extends BaseMapper<InquiryTask> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
    List<InquiryTaskResult> customList(@Param("paramCondition") InquiryTaskParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") InquiryTaskParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
    Page<InquiryTaskResult> customPageList(@Param("page") Page page, @Param("paramCondition") InquiryTaskParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") InquiryTaskParam paramCondition);

}
