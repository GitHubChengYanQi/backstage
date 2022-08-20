package cn.atsoft.dasheng.production.mapper;

import cn.atsoft.dasheng.production.entity.ProductionPickListsCart;
import cn.atsoft.dasheng.production.model.params.ProductionPickListsCartParam;
import cn.atsoft.dasheng.production.model.result.ProductionPickListsCartResult;
import cn.atsoft.dasheng.production.pojo.QuerryLockedParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 领料单详情表 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-25
 */
public interface ProductionPickListsCartMapper extends BaseMapper<ProductionPickListsCart> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    List<ProductionPickListsCartResult> customList(@Param("paramCondition") ProductionPickListsCartParam paramCondition);
    List<ProductionPickListsCartResult> selfCartList(@Param("paramCondition") ProductionPickListsCartParam paramCondition,@Param("userId") Long userId);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ProductionPickListsCartParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    Page<ProductionPickListsCartResult> customPageList(@Param("page") Page page, @Param("paramCondition") ProductionPickListsCartParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ProductionPickListsCartParam paramCondition);
    List<Long> lockInkind();
    Integer lockNumber(@Param("paramCondition") QuerryLockedParam param);

}
