package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.InstockList;
import cn.atsoft.dasheng.erp.model.params.InstockListParam;
import cn.atsoft.dasheng.erp.model.result.InstockListResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 入库清单 服务类
 * </p>
 *
 * @author song
 * @since 2021-10-06
 */
public interface InstockListService extends IService<InstockList> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-10-06
     */
    void add(InstockListParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-10-06
     */
    void delete(InstockListParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-10-06
     */
    void update(InstockListParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-10-06
     */
    InstockListResult findBySpec(InstockListParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-10-06
     */
    List<InstockListResult> findListBySpec(InstockListParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-10-06
     */
    PageInfo<InstockListResult> findPageBySpec(InstockListParam param);

    /**
     * 批量扫码入库
     *
     * @param param
     */
    void batchInstock(InstockListParam param);

    InstockList getEntity(InstockListParam param);

    void format(List<InstockListResult> data);
}
