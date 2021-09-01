package cn.atsoft.dasheng.portal.repairImage.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.repairImage.entity.RepairImage;
import cn.atsoft.dasheng.portal.repairImage.model.params.RepairImageParam;
import cn.atsoft.dasheng.portal.repairImage.model.result.RepairImageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 报修图片 服务类
 * </p>
 *
 * @author 1
 * @since 2021-09-01
 */
public interface RepairImageService extends IService<RepairImage> {

    /**
     * 新增
     *
     * @author 1
     * @Date 2021-09-01
     */
    void add(RepairImageParam param);

    /**
     * 删除
     *
     * @author 1
     * @Date 2021-09-01
     */
    void delete(RepairImageParam param);

    /**
     * 更新
     *
     * @author 1
     * @Date 2021-09-01
     */
    void update(RepairImageParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 1
     * @Date 2021-09-01
     */
    RepairImageResult findBySpec(RepairImageParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 1
     * @Date 2021-09-01
     */
    List<RepairImageResult> findListBySpec(RepairImageParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 1
     * @Date 2021-09-01
     */
     PageInfo<RepairImageResult> findPageBySpec(RepairImageParam param);

}
