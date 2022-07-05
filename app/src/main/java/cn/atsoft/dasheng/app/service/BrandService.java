package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.model.params.BrandParam;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 品牌表 服务类
 * </p>
 *
 * @author 1
 * @since 2021-07-14
 */
public interface BrandService extends IService<Brand> {

    /**
     * 新增
     *
     * @author 1
     * @Date 2021-07-14
     */
    Long add(BrandParam param);

    /**
     * 删除
     *
     * @author 1
     * @Date 2021-07-14
     */
    void delete(BrandParam param);

    /**
     * 更新
     *
     * @author 1
     * @Date 2021-07-14
     */
    void update(BrandParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 1
     * @Date 2021-07-14
     */
    BrandResult findBySpec(BrandParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 1
     * @Date 2021-07-14
     */
    List<BrandResult> findListBySpec(BrandParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 1
     * @Date 2021-07-14
     */
    PageInfo<BrandResult> findPageBySpec(BrandParam param, DataScope dataScope);

    PageInfo<BrandResult> pureList(BrandParam param, DataScope dataScope);

    /**
     * 返回多个品牌
     *
     * @param brandIds
     * @return
     */
    List<BrandResult> getBrandResults(List<Long> brandIds);


    BrandResult getBrandResult (Long id );

    void format(List<BrandResult> data);
}
