package cn.atsoft.dasheng.portal.class.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.class.entity.Class;
import cn.atsoft.dasheng.portal.class.model.params.ClassParam;
import cn.atsoft.dasheng.portal.class.model.result.ClassResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 分类导航 服务类
 * </p>
 *
 * @author siqiang
 * @since 2021-08-18
 */
public interface ClassService extends IService<Class> {

    /**
     * 新增
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    void add(ClassParam param);

    /**
     * 删除
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    void delete(ClassParam param);

    /**
     * 更新
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    void update(ClassParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    ClassResult findBySpec(ClassParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    List<ClassResult> findListBySpec(ClassParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author siqiang
     * @Date 2021-08-18
     */
     PageInfo<ClassResult> findPageBySpec(ClassParam param);

}
