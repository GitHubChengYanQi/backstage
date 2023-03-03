package cn.atsoft.dasheng.bom.mapper;

import cn.atsoft.dasheng.bom.entity.RestBomDetail;
import cn.atsoft.dasheng.bom.model.params.RestBomDetailParam;
import cn.atsoft.dasheng.bom.model.result.RestBomDetailResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RestBomDetailMapper extends BaseMapper<RestBomDetail> {
    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-07-25
     */
    List<RestBomDetailResult> customList(@Param("paramCondition") RestBomDetailParam bomDetailParam);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-07-25
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") RestBomDetailParam bomDetailParam);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-07-25
     */
    Page<RestBomDetailResult> customPageList(@Param("page") Page page, @Param("paramCondition") RestBomDetailParam bomDetailParam);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-07-25
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") RestBomDetailParam bomDetailParam);
}
