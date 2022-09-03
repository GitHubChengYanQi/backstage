package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.erp.entity.StorehousePositions;
import cn.atsoft.dasheng.erp.model.params.StorehousePositionsParam;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.erp.pojo.PositionLoop;
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
public interface StorehousePositionsService extends IService<StorehousePositions> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-10-29
     */
    void add(StorehousePositionsParam param);

    //判断当前库位是否为最下级
    Long judgePosition(Long positionId);


    List<StorehousePositionsResult> details(List<Long> positionIds);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-10-29
     */
    void delete(StorehousePositionsParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-10-29
     */
    void update(StorehousePositionsParam param);

    List<StorehousePositionsResult> selectBySku(StorehousePositionsParam param);

    Object selectBySku(Long skuId);

    List<BrandResult> selectByBrand(Long skuId, Long brandId, Long storehouseId, Long positionId);

    Integer getPositionNum(List<Long> skuIds);

    StorehousePositionsResult positionsResultById(Long codeId);


    List<PositionLoop> treeViewBySku(List<Long> skuIds);

    List<PositionLoop> treeViewByName(String name, Long houseId);

    void positionFormat(List<PositionLoop> positionLoops, Long skuId);

    List<Long> getLoopPositionIds(Long positionId);

    Map<Long, List<StorehousePositionsResult>> getMap(List<Long> skuIds);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-10-29
     */
    StorehousePositionsResult findBySpec(StorehousePositionsParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-10-29
     */
    List<StorehousePositionsResult> findListBySpec(StorehousePositionsParam param, DataScope dataScope);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-10-29
     */
    PageInfo<StorehousePositionsResult> findPageBySpec(StorehousePositionsParam param);


    StorehousePositionsResult getDetail(Long id, List<StorehousePositions> positions);


    List<StorehousePositionsResult> getDetails(List<Long> ids);

    List<StorehousePositionsResult> resultsByIds(List<Long> ids);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2021-10-29
     */

    Map<String, Map<String, Object>> takeStock(StorehousePositionsParam param);

    StorehousePositionsResult detail(Long id);

    List<StorehousePositionsResult> positionsResults(List<Long> ids);

    /**
     * 从下找上级库位 通过sku
     *
     * @param skuId
     * @return
     */
    List<StorehousePositionsResult> getSupperBySkuId(Long skuId);

    List<Long> getEndChild(Long positionId);

    List<Long> getEndChild(Long positionId, List<StorehousePositions> positions);

    Map<Long, Long> getHouseByPositionId(List<Long> postitionIds);

    void format(List<StorehousePositionsResult> data);

    void skuFormat(List<SkuResult> data);
}
