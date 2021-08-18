package cn.atsoft.dasheng.portal.classdifferencedetail.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.classdifferencedetail.entity.ClassDifferenceDetails;
import cn.atsoft.dasheng.portal.classdifferencedetail.model.params.ClassDifferenceDetailsParam;
import cn.atsoft.dasheng.portal.classdifferencedetail.model.result.ClassDifferenceDetailsResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 分类明细内容 服务类
 * </p>
 *
 * @author siqiang
 * @since 2021-08-18
 */
public interface ClassDifferenceDetailsService extends IService<ClassDifferenceDetails> {

    /**
     * 新增
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    void add(ClassDifferenceDetailsParam param);

    /**
     * 删除
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    void delete(ClassDifferenceDetailsParam param);

    /**
     * 更新
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    void update(ClassDifferenceDetailsParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    ClassDifferenceDetailsResult findBySpec(ClassDifferenceDetailsParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    List<ClassDifferenceDetailsResult> findListBySpec(ClassDifferenceDetailsParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author siqiang
     * @Date 2021-08-18
     */
     PageInfo<ClassDifferenceDetailsResult> findPageBySpec(ClassDifferenceDetailsParam param);

}
