package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Category;
import cn.atsoft.dasheng.erp.model.params.CategoryParam;

import cn.atsoft.dasheng.erp.model.result.CategoryResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 物品分类表 服务类
 * </p>
 *
 * @author jazz
 * @since 2021-10-18
 */
public interface CategoryService extends IService<Category> {


    /**
     * 新增
     *
     * @author jazz
     * @Date 2021-10-18
     */
    Long   add(CategoryParam param);

    /**
     * 删除
     *
     * @author jazz
     * @Date 2021-10-18
     */
    void delete(CategoryParam param);

    /**
     * 更新
     *
     * @author jazz
     * @Date 2021-10-18
     */
    void update(CategoryParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author jazz
     * @Date 2021-10-18
     */
    CategoryResult findBySpec(CategoryParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author jazz
     * @Date 2021-10-18
     */
    List<CategoryResult> findListBySpec(CategoryParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author jazz
     * @Date 2021-10-18
     */
     PageInfo<CategoryResult> findPageBySpec(CategoryParam param);

    void addList(CategoryParam param);
}
