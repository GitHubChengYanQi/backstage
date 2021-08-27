package cn.atsoft.dasheng.shop.classPage.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.shop.classPage.entity.Classpojo;
import cn.atsoft.dasheng.shop.classPage.model.params.ClassParam;
import cn.atsoft.dasheng.shop.classPage.model.result.ClassResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 一级分类导航 服务类
 * </p>
 *
 * @author 
 * @since 2021-08-19
 */
public interface ClassService extends IService<Classpojo> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-08-19
     */
    void add(ClassParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-08-19
     */
    void delete(ClassParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-08-19
     */
    void update(ClassParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-08-19
     */
    ClassResult findBySpec(ClassParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-08-19
     */
    List<ClassResult> findListBySpec(ClassParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-08-19
     */
     PageInfo<ClassResult> findPageBySpec(ClassParam param);
     void batchDelete (List<Long> ids);
     List format ();
}
