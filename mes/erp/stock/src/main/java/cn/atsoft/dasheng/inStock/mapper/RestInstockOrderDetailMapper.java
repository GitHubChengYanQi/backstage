package cn.atsoft.dasheng.inStock.mapper;


import cn.atsoft.dasheng.inStock.entity.RestInstockOrderDetail;
import cn.atsoft.dasheng.inStock.model.params.RestInstockOrderDetailParam;
import cn.atsoft.dasheng.inStock.model.result.RestInstockOrderDetailResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 入库清单 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-10-06
 */
public interface RestInstockOrderDetailMapper extends BaseMapper<RestInstockOrderDetail> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-10-06
     */
    List<RestInstockOrderDetailResult> customList(@Param("paramCondition") RestInstockOrderDetailParam paramCondition);


    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-10-06
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") RestInstockOrderDetailParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-10-06
     */
    Page<RestInstockOrderDetailResult> customPageList(@Param("page") Page page, @Param("paramCondition") RestInstockOrderDetailParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-10-06
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") RestInstockOrderDetailParam paramCondition);
}
