package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.ItemBrand;
import cn.atsoft.dasheng.app.model.params.ItemBrandParam;
import cn.atsoft.dasheng.app.model.result.ItemBrandResult;
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
 * @since 2021-09-23
 */
public interface ItemBrandMapper extends BaseMapper<ItemBrand> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-09-23
     */
    List<ItemBrandResult> customList(@Param("paramCondition") ItemBrandParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-09-23
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ItemBrandParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-09-23
     */
    Page<ItemBrandResult> customPageList(@Param("page") Page page, @Param("paramCondition") ItemBrandParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-09-23
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ItemBrandParam paramCondition);

}
