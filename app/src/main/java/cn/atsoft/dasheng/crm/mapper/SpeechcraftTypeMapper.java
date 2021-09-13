package cn.atsoft.dasheng.crm.mapper;

import cn.atsoft.dasheng.crm.entity.SpeechcraftType;
import cn.atsoft.dasheng.crm.model.params.SpeechcraftTypeParam;
import cn.atsoft.dasheng.crm.model.result.SpeechcraftTypeResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 话术分类 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-09-13
 */
public interface SpeechcraftTypeMapper extends BaseMapper<SpeechcraftType> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-09-13
     */
    List<SpeechcraftTypeResult> customList(@Param("paramCondition") SpeechcraftTypeParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-09-13
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") SpeechcraftTypeParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-09-13
     */
    Page<SpeechcraftTypeResult> customPageList(@Param("page") Page page, @Param("paramCondition") SpeechcraftTypeParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-09-13
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") SpeechcraftTypeParam paramCondition);

}
