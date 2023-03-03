package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.BomDetail;
import cn.atsoft.dasheng.erp.model.params.BomDetailParam;
import cn.atsoft.dasheng.erp.model.result.BomDetailResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BomDetailMapper extends BaseMapper<BomDetail> {
    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-07-25
     */
    List<BomDetailResult> customList(@Param("paramCondition") BomDetailParam bomDetailParam);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-07-25
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") BomDetailParam bomDetailParam);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-07-25
     */
    Page<BomDetailResult> customPageList(@Param("page") Page page, @Param("paramCondition") BomDetailParam bomDetailParam);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-07-25
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") BomDetailParam bomDetailParam);
}
