package cn.atsoft.dasheng.storehousePositionBind.service;


import cn.atsoft.dasheng.storehousePositionBind.entity.StorehousePositionsBind;
import cn.atsoft.dasheng.storehousePositionBind.model.result.StorehousePositionsBindResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 库位绑定物料表 服务类
 * </p>
 *
 * @author song
 * @since 2022-01-20
 */
public interface RestStorehousePositionsBindService extends IService<StorehousePositionsBind> {
    //
    //    @Override
    //    public List<StorehousePositionsResult> sku2position(Long skuId) {
    //        List<StorehousePositionsBind> positionsBinds = this.query().eq("sku_id", skuId).eq("display", 1).list();
    //
    //        List<Storehouse> storehouseList = storehouseService.list();
    //
    //        List<Long> positionIds = new ArrayList<>();
    //        for (StorehousePositionsBind positionsBind : positionsBinds) {
    //            positionIds.add(positionsBind.getPositionId());
    //        }
    //        List<StorehousePositionsDeptBindResult> bindByPositionIds = positionIds.size() > 0 ? storehousePositionsDeptBindService.getBindByPositionIds(positionIds) : new ArrayList<>();
    //        List<StorehousePositionsResult> results = new ArrayList<>();
    //
    //        positionIds.clear();
    //        Long deptId = LoginContextHolder.getContext().getUser().getDeptId();
    //        for (StorehousePositionsDeptBindResult bindByPositionId : bindByPositionIds) {
    //            if (bindByPositionId.getDeptIds().stream().anyMatch(i -> i.equals(deptId))) {
    //                positionIds.add(bindByPositionId.getStorehousePositionsId());
    //            }
    //        }
    //        List<StorehousePositions> storehousePositions = positionIds.size() > 0 ? positionsService.listByIds(positionIds) : new ArrayList<>();
    //        for (StorehousePositions storehousePosition : storehousePositions) {
    //            StorehousePositionsResult storehousePositionsResult = new StorehousePositionsResult();
    //            ToolUtil.copyProperties(storehousePosition, storehousePositionsResult);
    //            for (Storehouse storehouse : storehouseList) {
    //                if (storehouse.getStorehouseId().equals(storehousePosition.getStorehouseId())) {
    //                    StorehouseResult storehouseResult = new StorehouseResult();
    //                    ToolUtil.copyProperties(storehouse, storehouseResult);
    //                    storehousePositionsResult.setStorehouseResult(storehouseResult);
    //                }
    //            }
    //            results.add(storehousePositionsResult);
    //        }
    //
    //        return results;
    //    }
    //
    //
    //    private Serializable getKey(StorehousePositionsBindParam param) {
    //        return param.getBindId();
    //    }
    //
    //    private Page<StorehousePositionsBindResult> getPageContext() {
    //        return PageFactory.defaultPage();
    //    }
    //
    //    private StorehousePositionsBind getOldEntity(StorehousePositionsBindParam param) {
    //        return this.getById(getKey(param));
    //    }
    //
    //    private StorehousePositionsBind getEntity(StorehousePositionsBindParam param) {
    //        StorehousePositionsBind entity = new StorehousePositionsBind();
    //        ToolUtil.copyProperties(param, entity);
    //        return entity;
    //    }
    List<StorehousePositionsBindResult> resultBySkuIds(List<Long> skuIds);

//    /**
//     * 新增
//     *
//     * @author song
//     * @Date 2022-01-20
//     */
//    StorehousePositionsBind add(StorehousePositionsBindParam param);
//
//    void bindBatch(StorehousePositionsBindParam param);
//
//    void SpuAddBind(StorehousePositionsBindParam param);
//
//    /**
//     * 删除
//     *
//     * @author song
//     * @Date 2022-01-20
//     */
//    void delete(StorehousePositionsBindParam param);
//
//    /**
//     * 更新
//     *
//     * @author song
//     * @Date 2022-01-20
//     */
//    void update(StorehousePositionsBindParam param);
//
//    List<StorehousePositionsResult> treeView(List<Long> skuIds);
//
//    List<StorehousePositionsResult> bindTreeView(List<Long> skuIds);
//
//    /**
//     * 查询单条数据，Specification模式
//     *
//     * @author song
//     * @Date 2022-01-20
//     */
//    StorehousePositionsBindResult findBySpec(StorehousePositionsBindParam param);
//
//    /**
//     * 查询列表，Specification模式
//     *
//     * @author song
//     * @Date 2022-01-20
//     */
//    List<StorehousePositionsBindResult> findListBySpec(StorehousePositionsBindParam param);
//
//    /**
//     * 查询分页数据，Specification模式
//     *
//     * @author song
//     * @Date 2022-01-20
//     */
//    PageInfo<StorehousePositionsBindResult> findPageBySpec(StorehousePositionsBindParam param);
//
//
//    StorehousePositions getPosition(String name, List<StorehousePositions> positions);
//
//    StorehousePositionsBind judge(PositionBind excel, List<StorehousePositionsBind> positionsBinds);
//
//    Brand judgeBrand(String name, List<Brand> brands);
//
//    List<StorehousePositionsResult> sku2position(Long skuId);
}
