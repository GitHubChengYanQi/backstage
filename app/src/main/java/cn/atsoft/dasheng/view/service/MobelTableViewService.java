package cn.atsoft.dasheng.view.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.view.entity.MobelTableView;
import cn.atsoft.dasheng.view.model.params.MobelTableViewParam;
import cn.atsoft.dasheng.view.model.result.MobelTableViewResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 移动端菜单 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-05-09
 */
public interface MobelTableViewService extends IService<MobelTableView> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-05-09
     */
    void add(MobelTableViewParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-05-09
     */
    void delete(MobelTableViewParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-05-09
     */
    void update(MobelTableViewParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-05-09
     */
    MobelTableViewResult findBySpec(MobelTableViewParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-05-09
     */
    List<MobelTableViewResult> findListBySpec(MobelTableViewParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-05-09
     */
     PageInfo<MobelTableViewResult> findPageBySpec(MobelTableViewParam param);

}
