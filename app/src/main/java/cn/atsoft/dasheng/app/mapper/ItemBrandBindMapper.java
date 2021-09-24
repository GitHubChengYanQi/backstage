package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.ItemBrandBind;
import cn.atsoft.dasheng.app.model.params.ItemBrandBindParam;
import cn.atsoft.dasheng.app.model.result.ItemBrandBindResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品品牌绑定表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-09-24
 */
public interface ItemBrandBindMapper extends BaseMapper<ItemBrandBind> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-09-24
     */
    List<ItemBrandBindResult> customList(@Param("paramCondition") ItemBrandBindParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-09-24
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ItemBrandBindParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-09-24
     */
    Page<ItemBrandBindResult> customPageList(@Param("page") Page page, @Param("paramCondition") ItemBrandBindParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-09-24
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ItemBrandBindParam paramCondition);

}
