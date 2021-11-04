package cn.atsoft.dasheng.view.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.view.entity.TableView;
import cn.atsoft.dasheng.view.model.params.TableViewParam;
import cn.atsoft.dasheng.view.model.result.TableViewResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2021-11-04
 */
public interface TableViewService extends IService<TableView> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-11-04
     */
    void add(TableViewParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-11-04
     */
    void delete(TableViewParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-11-04
     */
    void update(TableViewParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-11-04
     */
    TableViewResult findBySpec(TableViewParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-11-04
     */
    List<TableViewResult> findListBySpec(TableViewParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-11-04
     */
     PageInfo<TableViewResult> findPageBySpec(TableViewParam param);

}
