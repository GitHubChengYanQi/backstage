package cn.atsoft.dasheng.goods.classz.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.goods.classz.entity.RestAttribute;
import cn.atsoft.dasheng.goods.classz.model.params.RestAttributeParam;
import cn.atsoft.dasheng.goods.classz.model.result.RestAttributeResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 产品属性表 服务类
 * </p>
 *
 * @author song
 * @since 2021-10-18
 */
public interface RestAttributeService extends IService<RestAttribute> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-10-18
     */
    Long add(RestAttributeParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-10-18
     */
    void delete(RestAttributeParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-10-18
     */
    void update(RestAttributeParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-10-18
     */
    RestAttributeResult findBySpec(RestAttributeParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-10-18
     */
    List<RestAttributeResult> findListBySpec(RestAttributeParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-10-18
     */
    PageInfo<RestAttributeResult> findPageBySpec(RestAttributeParam param);

    List<RestAttribute> restAttributeByCategoryId(Long CategoryId);

}
