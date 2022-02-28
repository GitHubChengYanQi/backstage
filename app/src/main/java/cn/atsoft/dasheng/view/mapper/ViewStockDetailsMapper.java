package cn.atsoft.dasheng.view.mapper;

import cn.atsoft.dasheng.view.entity.ViewStockDetails;
import cn.atsoft.dasheng.view.model.params.TableViewParam;
import cn.atsoft.dasheng.view.model.params.ViewStockDetailsParam;
import cn.atsoft.dasheng.view.model.result.TableViewResult;
import cn.atsoft.dasheng.view.model.result.ViewStockDetailsResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * VIEW Mapper 接口
 * </p>
 *
 * @author 
 * @since 2022-01-27
 */
public interface ViewStockDetailsMapper extends BaseMapper<ViewStockDetails> {

    /**
     * 获取产品列表
     *
     * @author
     * @Date 2021-11-04
     */
    List<ViewStockDetailsResult> classNameList(@Param("paramCondition") ViewStockDetailsParam paramCondition);

    /**
     * 获取型号列表
     *
     * @author
     * @Date 2021-11-04
     */
    List<ViewStockDetailsResult> spuList(@Param("paramCondition") ViewStockDetailsParam paramCondition);

    /**
     * 获取物料列表
     *
     * @author
     * @Date 2021-11-04
     */
    List<ViewStockDetailsResult> skuList(@Param("paramCondition") ViewStockDetailsParam paramCondition);

    /**  获取bom列表
     *
     * @param paramCondition
     * @return
     */
    List<ViewStockDetailsResult> bomList(@Param("paramCondition") ViewStockDetailsParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2022-01-27
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ViewStockDetailsParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2022-01-27
     */
    Page<ViewStockDetailsResult> customPageList(@Param("page") Page page, @Param("paramCondition") ViewStockDetailsParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2022-01-27
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ViewStockDetailsParam paramCondition);

}
