package cn.atsoft.dasheng.goods.brand.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.goods.brand.entity.RestBrand;
import cn.atsoft.dasheng.goods.brand.model.params.RestBrandParam;
import cn.atsoft.dasheng.goods.brand.model.result.RestBrandResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface RestBrandService extends IService<RestBrand> {
    Long add(RestBrandParam param);

    /**
     * 删除
     *
     * @author 1
     * @Date 2021-07-14
     */
    void delete(RestBrandParam param);

    /**
     * 更新
     *
     * @author 1
     * @Date 2021-07-14
     */
    void update(RestBrandParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 1
     * @Date 2021-07-14
     */
    RestBrandResult findBySpec(RestBrandParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 1
     * @Date 2021-07-14
     */
    List<RestBrandResult> findListBySpec(RestBrandParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 1
     * @Date 2021-07-14
     */
    PageInfo findPageBySpec(RestBrandParam param, DataScope dataScope);

    PageInfo pureList(RestBrandParam param, DataScope dataScope);

    /**
     * 返回多个品牌
     *
     * @param brandIds
     * @return
     */
    List<RestBrandResult> getBrandResults(List<Long> brandIds);


    RestBrandResult getBrandResult (Long id );

    void format(List<RestBrandResult> data);
}
