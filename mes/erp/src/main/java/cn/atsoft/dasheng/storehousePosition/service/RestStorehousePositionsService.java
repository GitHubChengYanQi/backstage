package cn.atsoft.dasheng.storehousePosition.service;

//import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
//import cn.atsoft.dasheng.erp.entity.StorehousePositions;
//import cn.atsoft.dasheng.erp.model.params.StorehousePositionsParam;
//import cn.atsoft.dasheng.erp.model.result.SkuResult;
//import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
//import cn.atsoft.dasheng.erp.pojo.PositionLoop;
import cn.atsoft.dasheng.storehousePosition.entity.RestStorehousePositions;
import cn.atsoft.dasheng.storehousePosition.model.params.RestStorehousePositionsParam;
import cn.atsoft.dasheng.storehousePosition.model.result.RestStorehousePositionsResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 仓库库位表 服务类
 * </p>
 *
 * @author song
 * @since 2021-10-29
 */
public interface RestStorehousePositionsService extends IService<RestStorehousePositions> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-10-29
     */
    void add(RestStorehousePositionsParam param);

    //判断当前库位是否为最下级
    Long judgePosition(Long positionId);


    List<RestStorehousePositionsResult> details(List<Long> positionIds);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-10-29
     */
    void delete(RestStorehousePositionsParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-10-29
     */
    void update(RestStorehousePositionsParam param);

    List<RestStorehousePositionsResult> selectBySku(RestStorehousePositionsParam param);

    Object selectBySku(Long skuId);

//    List<BrandResult> selectByBrand(Long skuId, Long brandId, Long storehouseId, Long positionId);

    Integer getPositionNum(List<Long> skuIds);

    RestStorehousePositionsResult positionsResultById(Long codeId);

//
//    List<PositionLoop> treeViewBySku(List<Long> skuIds);
//
//    List<PositionLoop> treeViewByName(String name, Long houseId);
//
//    void positionFormat(List<PositionLoop> positionLoops, Long skuId);

    List<Long> getLoopPositionIds(Long positionId);

    Map<Long, List<RestStorehousePositionsResult>> getMap(List<Long> skuIds);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-10-29
     */
    RestStorehousePositionsResult findBySpec(RestStorehousePositionsParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-10-29
     */
    List<RestStorehousePositionsResult> findListBySpec(RestStorehousePositionsParam param, DataScope dataScope);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-10-29
     */
    PageInfo<RestStorehousePositionsResult> findPageBySpec(RestStorehousePositionsParam param);


    RestStorehousePositionsResult getDetail(Long id, List<RestStorehousePositions> positions);


    List<RestStorehousePositionsResult> getDetails(List<Long> ids);

    List<RestStorehousePositionsResult> resultsByIds(List<Long> ids);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2021-10-29
     */

    Map<String, Map<String, Object>> takeStock(RestStorehousePositionsParam param);

    RestStorehousePositionsResult detail(Long id);

    List<RestStorehousePositionsResult> positionsResults(List<Long> ids);

    /**
     * 从下找上级库位 通过sku
     *
     * @param skuId
     * @return
     */
    List<RestStorehousePositionsResult> getSupperBySkuId(Long skuId);

    List<Long> getEndChild(Long positionId);

    List<Long> getEndChild(Long positionId, List<RestStorehousePositions> positions);

    Map<Long, Long> getHouseByPositionId(List<Long> postitionIds);

    void format(List<RestStorehousePositionsResult> data);

//    void skuFormat(List<SkuResult> data);
}
