package cn.atsoft.dasheng.supplier.mapper;

import cn.atsoft.dasheng.supplier.entity.SupplierBrand;
import cn.atsoft.dasheng.supplier.model.params.SupplierBrandParam;
import cn.atsoft.dasheng.supplier.model.result.SupplierBrandResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 供应商品牌绑定 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2022-01-13
 */
public interface SupplierBrandMapper extends BaseMapper<SupplierBrand> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2022-01-13
     */
    List<SupplierBrandResult> customList(@Param("paramCondition") SupplierBrandParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2022-01-13
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") SupplierBrandParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2022-01-13
     */
    Page<SupplierBrandResult> customPageList(@Param("page") Page page, @Param("paramCondition") SupplierBrandParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2022-01-13
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") SupplierBrandParam paramCondition);

}
