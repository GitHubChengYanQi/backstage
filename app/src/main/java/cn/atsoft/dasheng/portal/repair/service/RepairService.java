package cn.atsoft.dasheng.portal.repair.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.repair.entity.Repair;
import cn.atsoft.dasheng.portal.repair.model.params.RepairParam;
import cn.atsoft.dasheng.portal.repair.model.result.RepairResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 报修 服务类
 * </p>
 *
 * @author siqiang
 * @since 2021-08-20
 */
public interface RepairService extends IService<Repair> {

    /**
     * 新增
     *
     * @return
     * @author siqiang
     * @Date 2021-08-20
     */
    Repair add(RepairParam param);

    /**
     * 删除
     *
     * @author siqiang
     * @Date 2021-08-20
     */
    void delete(RepairParam param);

    /**
     * 更新
     *
     * @author siqiang
     * @Date 2021-08-20
     */
    Repair update(RepairParam param);

    String updatedynamic(RepairParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author siqiang
     * @Date 2021-08-20
     */
    RepairResult findBySpec(RepairParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author siqiang
     * @Date 2021-08-20
     */
    List<RepairResult> findListBySpec(RepairParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author siqiang
     * @Date 2021-08-20
     */
    PageInfo<RepairResult> findPageBySpec(RepairParam param);

    RepairResult detail(Long id);

    RepairResult format(List<RepairResult> data);

    void getWxMap (RepairParam param);


}
