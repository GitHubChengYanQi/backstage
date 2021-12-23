package cn.atsoft.dasheng.purchase.mapper;

import cn.atsoft.dasheng.purchase.entity.InquirtyComment;
import cn.atsoft.dasheng.purchase.model.params.InquirtyCommentParam;
import cn.atsoft.dasheng.purchase.model.result.InquirtyCommentResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-23
 */
public interface InquirtyCommentMapper extends BaseMapper<InquirtyComment> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
    List<InquirtyCommentResult> customList(@Param("paramCondition") InquirtyCommentParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") InquirtyCommentParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
    Page<InquirtyCommentResult> customPageList(@Param("page") Page page, @Param("paramCondition") InquirtyCommentParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") InquirtyCommentParam paramCondition);

}
