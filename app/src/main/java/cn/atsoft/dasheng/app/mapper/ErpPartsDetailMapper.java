package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.ErpPartsDetail;
import cn.atsoft.dasheng.app.model.params.ErpPartsDetailParam;
import cn.atsoft.dasheng.app.model.result.ErpPartsDetailResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 清单详情 Mapper 接口
 * </p>
 *
 * @author cheng
 * @since 2021-10-26
 */
public interface ErpPartsDetailMapper extends BaseMapper<ErpPartsDetail> {

    /**
     * 获取列表
     *
     * @author cheng
     * @Date 2021-10-26
     */
    List<ErpPartsDetailResult> customList(@Param("paramCondition") ErpPartsDetailParam paramCondition);

    /**
     * 获取map列表
     *
     * @author cheng
     * @Date 2021-10-26
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ErpPartsDetailParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author cheng
     * @Date 2021-10-26
     */
    Page<ErpPartsDetailResult> customPageList(@Param("page") Page page, @Param("paramCondition") ErpPartsDetailParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author cheng
     * @Date 2021-10-26
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ErpPartsDetailParam paramCondition);


    Page<ErpPartsDetailResult> pageBomList(@Param("page") Page page, @Param("paramCondition") ErpPartsDetailParam paramCondition);




}
