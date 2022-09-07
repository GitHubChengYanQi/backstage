package cn.atsoft.dasheng.printTemplate.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.printTemplate.entity.PrintTemplate;
import cn.atsoft.dasheng.printTemplate.model.params.PrintTemplateParam;
import cn.atsoft.dasheng.printTemplate.model.result.PrintTemplateResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 编辑模板 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-28
 */
public interface PrintTemplateService extends IService<PrintTemplate> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2021-12-28
     */
    void add(PrintTemplateParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2021-12-28
     */
    void delete(PrintTemplateParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2021-12-28
     */
    void update(PrintTemplateParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2021-12-28
     */
    PrintTemplateResult findBySpec(PrintTemplateParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2021-12-28
     */
    List<PrintTemplateResult> findListBySpec(PrintTemplateParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2021-12-28
     */
     PageInfo<PrintTemplateResult> findPageBySpec(DataScope dataScope , PrintTemplateParam param);


    String replaceInkindTemplate( Long inkindId);
}
