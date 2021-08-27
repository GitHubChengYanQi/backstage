package cn.atsoft.dasheng.portal.repairDynamic.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.repairDynamic.entity.RepairDynamic;
import cn.atsoft.dasheng.portal.repairDynamic.model.params.RepairDynamicParam;
import cn.atsoft.dasheng.portal.repairDynamic.model.result.RepairDynamicResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 售后动态表 服务类
 * </p>
 *
 * @author 
 * @since 2021-08-24
 */
public interface RepairDynamicService extends IService<RepairDynamic> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-08-24
     */
    void add(RepairDynamicParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-08-24
     */
    void delete(RepairDynamicParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-08-24
     */
    void update(RepairDynamicParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-08-24
     */
    RepairDynamicResult findBySpec(RepairDynamicParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-08-24
     */
    List<RepairDynamicResult> findListBySpec(RepairDynamicParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-08-24
     */
     PageInfo<RepairDynamicResult> findPageBySpec(RepairDynamicParam param);

}
