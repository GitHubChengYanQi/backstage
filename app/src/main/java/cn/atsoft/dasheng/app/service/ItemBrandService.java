package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.ItemBrand;
import cn.atsoft.dasheng.app.model.params.ItemBrandParam;
import cn.atsoft.dasheng.app.model.result.ItemBrandResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品品牌绑定表 服务类
 * </p>
 *
 * @author 
 * @since 2021-09-23
 */
public interface ItemBrandService extends IService<ItemBrand> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-09-23
     */
    void add(ItemBrandParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-09-23
     */
    void delete(ItemBrandParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-09-23
     */
    void update(ItemBrandParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-09-23
     */
    ItemBrandResult findBySpec(ItemBrandParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-09-23
     */
    List<ItemBrandResult> findListBySpec(ItemBrandParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-09-23
     */
     PageInfo findPageBySpec(ItemBrandParam param, DataScope dataScope );

}
