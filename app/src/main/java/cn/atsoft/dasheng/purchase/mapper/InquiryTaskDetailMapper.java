package cn.atsoft.dasheng.purchase.mapper;

import cn.atsoft.dasheng.purchase.entity.InquiryTaskDetail;
import cn.atsoft.dasheng.purchase.model.params.InquiryTaskDetailParam;
import cn.atsoft.dasheng.purchase.model.result.InquiryTaskDetailResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 询价任务详情 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-23
 */
public interface InquiryTaskDetailMapper extends BaseMapper<InquiryTaskDetail> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
    List<InquiryTaskDetailResult> customList(@Param("paramCondition") InquiryTaskDetailParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") InquiryTaskDetailParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
    Page<InquiryTaskDetailResult> customPageList(@Param("page") Page page, @Param("paramCondition") InquiryTaskDetailParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") InquiryTaskDetailParam paramCondition);

}
