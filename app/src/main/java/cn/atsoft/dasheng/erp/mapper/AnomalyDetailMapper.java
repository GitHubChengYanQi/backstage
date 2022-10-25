package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.AnomalyDetail;
import cn.atsoft.dasheng.erp.model.params.AnomalyDetailParam;
import cn.atsoft.dasheng.erp.model.result.AnomalyDetailResult;
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
 * @author song
 * @since 2022-05-27
 */
public interface AnomalyDetailMapper extends BaseMapper<AnomalyDetail> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2022-05-27
     */
    List<AnomalyDetailResult> customList(@Param("paramCondition") AnomalyDetailParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2022-05-27
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") AnomalyDetailParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2022-05-27
     */
    Page<AnomalyDetailResult> customPageList(@Param("page") Page page, @Param("paramCondition") AnomalyDetailParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2022-05-27
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") AnomalyDetailParam paramCondition);


    List<AnomalyDetailResult> orderError(@Param("paramCondition") AnomalyDetailParam paramCondition);

}
