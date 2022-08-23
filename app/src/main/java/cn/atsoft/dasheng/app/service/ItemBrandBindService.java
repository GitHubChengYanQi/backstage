package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.ItemBrandBind;
import cn.atsoft.dasheng.app.model.params.ItemBrandBindParam;
import cn.atsoft.dasheng.app.model.result.ItemBrandBindResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品品牌绑定表 服务类
 * </p>
 *
 * @author 
 * @since 2021-09-24
 */
public interface ItemBrandBindService extends IService<ItemBrandBind> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-09-24
     */
    void add(ItemBrandBindParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-09-24
     */
    void delete(ItemBrandBindParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-09-24
     */
    void update(ItemBrandBindParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-09-24
     */
    ItemBrandBindResult findBySpec(ItemBrandBindParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-09-24
     */
    List<ItemBrandBindResult> findListBySpec(ItemBrandBindParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-09-24
     */
     PageInfo findPageBySpec(ItemBrandBindParam param, DataScope dataScope );

}
