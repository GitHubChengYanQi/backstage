package cn.atsoft.dasheng.shop.classDifference.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.shop.classDifference.entity.ClassDifference;
import cn.atsoft.dasheng.shop.classDifference.model.params.ClassDifferenceParam;
import cn.atsoft.dasheng.shop.classDifference.model.result.ClassDifferenceResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 分类明细 服务类
 * </p>
 *
 * @author siqiang
 * @since 2021-08-18
 */
public interface ClassDifferenceService extends IService<ClassDifference> {

    /**
     * 新增
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    void add(ClassDifferenceParam param);

    /**
     * 删除
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    void delete(ClassDifferenceParam param);

    /**
     * 更新
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    void update(ClassDifferenceParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    ClassDifferenceResult findBySpec(ClassDifferenceParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    List<ClassDifferenceResult> findListBySpec(ClassDifferenceParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    PageInfo<ClassDifferenceResult> findPageBySpec(ClassDifferenceParam param);


    List<ClassDifferenceResult> getByIds(List<Long> ids);

    List<ClassDifferenceResult> getdetalis(Long ids);
}
