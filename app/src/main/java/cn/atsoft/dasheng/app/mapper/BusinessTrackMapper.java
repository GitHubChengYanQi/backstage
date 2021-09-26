package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.BusinessTrack;
import cn.atsoft.dasheng.app.model.params.BusinessTrackParam;
import cn.atsoft.dasheng.app.model.result.BusinessTrackResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 跟进内容 Mapper 接口
 * </p>
 *
 * @author cheng
 * @since 2021-09-17
 */
public interface BusinessTrackMapper extends BaseMapper<BusinessTrack> {

    /**
     * 获取列表
     *
     * @author cheng
     * @Date 2021-09-17
     */
    List<BusinessTrackResult> customList(@Param("paramCondition") BusinessTrackParam paramCondition);

    /**
     * 获取map列表
     *
     * @author cheng
     * @Date 2021-09-17
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") BusinessTrackParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author cheng
     * @Date 2021-09-17
     */
    Page<BusinessTrackResult> customPageList(List<Long> trackMessageIds,@Param("page") Page page, @Param("paramCondition") BusinessTrackParam paramCondition,@Param("dataScope") DataScope dataScope);

    /**
     * 获取分页map列表
     *
     * @author cheng
     * @Date 2021-09-17
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") BusinessTrackParam paramCondition);



}
