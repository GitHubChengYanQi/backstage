package cn.atsoft.dasheng.crm.mapper;

import cn.atsoft.dasheng.crm.entity.SpeechcraftTypeDetail;
import cn.atsoft.dasheng.crm.model.params.SpeechcraftTypeDetailParam;
import cn.atsoft.dasheng.crm.model.result.SpeechcraftTypeDetailResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 话术分类详细 Mapper 接口
 * </p>
 *
 * @author cheng
 * @since 2021-09-13
 */
public interface SpeechcraftTypeDetailMapper extends BaseMapper<SpeechcraftTypeDetail> {

    /**
     * 获取列表
     *
     * @author cheng
     * @Date 2021-09-13
     */
    List<SpeechcraftTypeDetailResult> customList(@Param("paramCondition") SpeechcraftTypeDetailParam paramCondition);

    /**
     * 获取map列表
     *
     * @author cheng
     * @Date 2021-09-13
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") SpeechcraftTypeDetailParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author cheng
     * @Date 2021-09-13
     */
    Page<SpeechcraftTypeDetailResult> customPageList(@Param("page") Page page, @Param("paramCondition") SpeechcraftTypeDetailParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author cheng
     * @Date 2021-09-13
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") SpeechcraftTypeDetailParam paramCondition);

}
