package cn.atsoft.dasheng.form.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.RestFormStyle;
import cn.atsoft.dasheng.form.model.params.RestFormStyleParam;
import cn.atsoft.dasheng.form.model.result.RestFormStyleResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 表单风格 服务类
 * </p>
 *
 * @author 
 * @since 2022-09-23
 */
public interface RestFormStyleService extends IService<RestFormStyle> {

    /**
     * 新增
     *
     * @author 
     * @Date 2022-09-23
     */
    void add(RestFormStyleParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2022-09-23
     */
    void delete(RestFormStyleParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2022-09-23
     */
    void update(RestFormStyleParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2022-09-23
     */
    RestFormStyleResult findBySpec(RestFormStyleParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2022-09-23
     */
    List<RestFormStyleResult> findListBySpec(RestFormStyleParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2022-09-23
     */
     PageInfo<RestFormStyleResult> findPageBySpec(RestFormStyleParam param);

}
