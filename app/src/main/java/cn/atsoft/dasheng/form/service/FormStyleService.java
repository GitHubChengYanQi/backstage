package cn.atsoft.dasheng.form.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.FormStyle;
import cn.atsoft.dasheng.form.model.params.FormStyleParam;
import cn.atsoft.dasheng.form.model.result.FormStyleResult;
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
public interface FormStyleService extends IService<FormStyle> {

    /**
     * 新增
     *
     * @author 
     * @Date 2022-09-23
     */
    void add(FormStyleParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2022-09-23
     */
    void delete(FormStyleParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2022-09-23
     */
    void update(FormStyleParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2022-09-23
     */
    FormStyleResult findBySpec(FormStyleParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2022-09-23
     */
    List<FormStyleResult> findListBySpec(FormStyleParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2022-09-23
     */
     PageInfo<FormStyleResult> findPageBySpec(FormStyleParam param);

}
