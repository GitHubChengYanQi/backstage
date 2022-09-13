package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.app.pojo.Lable;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Template;
import cn.atsoft.dasheng.app.model.params.TemplateParam;
import cn.atsoft.dasheng.app.model.result.TemplateResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 合同模板 服务类
 * </p>
 *
 * @author
 * @since 2021-07-21
 */
public interface TemplateService extends IService<Template> {

    /**
     * 新增
     *
     * @author
     * @Date 2021-07-21
     */
    Long add(TemplateParam param);

    /**
     * 删除
     *
     * @author
     * @Date 2021-07-21
     */
    void delete(TemplateParam param);

    /**
     * 更新
     *
     * @author
     * @Date 2021-07-21
     */
    void update(TemplateParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author
     * @Date 2021-07-21
     */
    TemplateResult findBySpec(TemplateParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author
     * @Date 2021-07-21
     */
    List<TemplateResult> findListBySpec(TemplateParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author
     * @Date 2021-07-21
     */
    PageInfo findPageBySpec(TemplateParam param, DataScope dataScope );

    /**
     * 批量删除
     * @param ids
     */
    void batchDelete(List<Long> ids);

    Lable getLabel(Long id);
}
