package cn.atsoft.dasheng.goods.category.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.goods.category.entity.RestCategory;
import cn.atsoft.dasheng.goods.category.model.params.RestCategoryParam;
import cn.atsoft.dasheng.goods.category.model.result.RestCategoryResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * SPU分类 服务类
 * </p>
 *
 * @author song
 * @since 2021-10-25
 */
public interface RestCategoryService extends IService<RestCategory> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-10-25
     */
    Long add(RestCategoryParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-10-25
     */
    void delete(RestCategoryParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-10-25
     */
    void update(RestCategoryParam param);

    List<String> getClassName();

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-10-25
     */
    RestCategoryResult findBySpec(RestCategoryParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-10-25
     */
    List<RestCategoryResult> findListBySpec(RestCategoryParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-10-25
     */
     PageInfo<RestCategoryResult> findPageBySpec(RestCategoryParam param);

    String getCodings(Long classId);
}
