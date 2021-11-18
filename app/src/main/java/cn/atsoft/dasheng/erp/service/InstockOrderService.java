package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.InstockOrder;
import cn.atsoft.dasheng.erp.model.params.InstockOrderParam;
import cn.atsoft.dasheng.erp.model.request.InstockParams;
import cn.atsoft.dasheng.erp.model.result.InstockOrderResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 入库单 服务类
 * </p>
 *
 * @author song
 * @since 2021-10-06
 */
public interface InstockOrderService extends IService<InstockOrder> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-10-06
     */
    void add(InstockOrderParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-10-06
     */
    void delete(InstockOrderParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-10-06
     */
    void update(InstockOrderParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-10-06
     */
    InstockOrderResult findBySpec(InstockOrderParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-10-06
     */
    List<InstockOrderResult> findListBySpec(InstockOrderParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-10-06
     */
    PageInfo<InstockOrderResult> findPageBySpec(InstockOrderParam param);


    /**
     * 通过质检创建入库单
     *
     * @param instockParams
     */
    void addByQuality(InstockParams instockParams);

}
