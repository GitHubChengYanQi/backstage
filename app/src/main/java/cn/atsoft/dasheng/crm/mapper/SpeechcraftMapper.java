package cn.atsoft.dasheng.crm.mapper;

import cn.atsoft.dasheng.crm.entity.Speechcraft;
import cn.atsoft.dasheng.crm.model.params.SpeechcraftParam;
import cn.atsoft.dasheng.crm.model.result.SpeechcraftResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 话术基础资料 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-09-11
 */
public interface SpeechcraftMapper extends BaseMapper<Speechcraft> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-09-11
     */
    List<SpeechcraftResult> customList(@Param("paramCondition") SpeechcraftParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-09-11
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") SpeechcraftParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-09-11
     */
    Page<SpeechcraftResult> customPageList(@Param("page") Page page, @Param("paramCondition") SpeechcraftParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-09-11
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") SpeechcraftParam paramCondition);

}
