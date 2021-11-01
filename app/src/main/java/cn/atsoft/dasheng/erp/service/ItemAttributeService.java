package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.ItemAttribute;
import cn.atsoft.dasheng.erp.model.params.ItemAttributeParam;
import cn.atsoft.dasheng.erp.model.result.ItemAttributeResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 产品属性表 服务类
 * </p>
 *
 * @author song
 * @since 2021-10-18
 */
public interface ItemAttributeService extends IService<ItemAttribute> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-10-18
     */
    Long add(ItemAttributeParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-10-18
     */
    void delete(ItemAttributeParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-10-18
     */
    void update(ItemAttributeParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-10-18
     */
    ItemAttributeResult findBySpec(ItemAttributeParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-10-18
     */
    List<ItemAttributeResult> findListBySpec(ItemAttributeParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-10-18
     */
     PageInfo<ItemAttributeResult> findPageBySpec(ItemAttributeParam param);

}
