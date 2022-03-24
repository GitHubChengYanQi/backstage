package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.Storehouse;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.app.service.StorehouseService;
import cn.atsoft.dasheng.base.auth.context.LoginContext;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.entity.StorehousePositions;
import cn.atsoft.dasheng.erp.entity.StorehousePositionsBind;
import cn.atsoft.dasheng.erp.mapper.StorehousePositionsBindMapper;
import cn.atsoft.dasheng.erp.model.params.StorehousePositionsBindParam;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsBindResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsDeptBindResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsBindService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.StorehousePositionsDeptBindService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 库位绑定物料表 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-01-20
 */
@Service
public class StorehousePositionsBindServiceImpl extends ServiceImpl<StorehousePositionsBindMapper, StorehousePositionsBind> implements StorehousePositionsBindService {
    @Autowired
    private StorehousePositionsService positionsService;

    @Autowired
    private StorehouseService storehouseService;

    @Autowired
    private SkuService skuService;

    @Autowired
    private StorehousePositionsDeptBindService storehousePositionsDeptBindService;

    @Override
    public StorehousePositionsBind add(StorehousePositionsBindParam param) {
        List<StorehousePositions> positions = positionsService.query().eq("pid", param.getPositionId()).list();
        if (ToolUtil.isNotEmpty(positions)) {
            throw new ServiceException(500, "当前库位不是最下级");
        }
        StorehousePositionsBind positionsBind = this.query().eq("position_id", param.getPositionId()).eq("sku_id", param.getSkuId()).one();
        if (ToolUtil.isNotEmpty(positionsBind)) {
            throw new ServiceException(500, "以绑定");
        }
        StorehousePositionsBind entity = getEntity(param);
        this.save(entity);
        return entity;
    }

    @Override
    public void bindBatch(StorehousePositionsBindParam param) {
        List<StorehousePositionsBind> binds = new ArrayList<>();
        for (Long skuId : param.getSkuIds()) {
            StorehousePositionsBind bind = new StorehousePositionsBind();
            bind.setSkuId(skuId);
            bind.setBrandId(param.getBrandId());
            bind.setPositionId(param.getPositionId());
            binds.add(bind);
        }
        this.saveBatch(binds);
    }

    @Override
    public void SpuAddBind(StorehousePositionsBindParam param) {

        List<StorehousePositions> positions = positionsService.query().eq("pid", param.getPositionId()).eq("display", 1).list();
        if (ToolUtil.isNotEmpty(positions)) {
            throw new ServiceException(500, "当前库位不是最下级");
        }
        List<StorehousePositionsBind> storehousePositionsBinds = new ArrayList<>();

        List<Sku> skus = skuService.query().eq("spu_id", param.getSpuId()).eq("display", 1).list();
        List<StorehousePositionsBind> positionsBinds = this.query().eq("position_id", param.getPositionId()).eq("display", 1).list();
        for (Sku sku : skus) {
            if (positionsBinds.stream().noneMatch(i -> i.getPositionId().equals(param.getPositionId()) && i.getSkuId().equals(sku.getSkuId()))) {
                StorehousePositionsBind bind = new StorehousePositionsBind();
                bind.setSkuId(sku.getSkuId());
                bind.setPositionId(param.getPositionId());
                storehousePositionsBinds.add(bind);
            }
        }
        this.saveBatch(storehousePositionsBinds);
    }

    @Override
    public void delete(StorehousePositionsBindParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(StorehousePositionsBindParam param) {
        StorehousePositionsBind oldEntity = getOldEntity(param);
        StorehousePositionsBind newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public StorehousePositionsBindResult findBySpec(StorehousePositionsBindParam param) {
        return null;
    }

    @Override
    public List<StorehousePositionsBindResult> findListBySpec(StorehousePositionsBindParam param) {
        return null;
    }

    @Override
    public PageInfo<StorehousePositionsBindResult> findPageBySpec(StorehousePositionsBindParam param) {
        Page<StorehousePositionsBindResult> pageContext = getPageContext();
        IPage<StorehousePositionsBindResult> page = this.baseMapper.customPageList(pageContext, param);
        this.format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    public void format(List<StorehousePositionsBindResult> param) {
        List<Long> skuIds = new ArrayList<>();
        List<Long> positionIds = new ArrayList<>();

        for (StorehousePositionsBindResult bindResult : param) {
            skuIds.add(bindResult.getSkuId());
            positionIds.add(bindResult.getPositionId());
        }

        List<SkuResult> skuResults = skuIds.size() == 0 ? new ArrayList<>() : skuService.formatSkuResult(skuIds);
        List<StorehousePositions> storehousePositions = positionIds.size() == 0 ? new ArrayList<>() : positionsService.listByIds(positionIds);
        List<StorehousePositionsResult> storehousePositionsResults = new ArrayList<>();
        for (StorehousePositions storehousePosition : storehousePositions) {
            StorehousePositionsResult storehousePositionsResult = new StorehousePositionsResult();
            ToolUtil.copyProperties(storehousePosition, storehousePositionsResult);
            storehousePositionsResults.add(storehousePositionsResult);
        }

        for (StorehousePositionsBindResult storehousePositionsBindResult : param) {
            for (SkuResult skuResult : skuResults) {
                if (storehousePositionsBindResult.getSkuId().equals(skuResult.getSkuId())) {
                    storehousePositionsBindResult.setSkuResult(skuResult);
                }
            }
            for (StorehousePositionsResult storehousePositionResult : storehousePositionsResults) {
                if (storehousePositionsBindResult.getPositionId().equals(storehousePositionResult.getStorehousePositionsId())) {
                    storehousePositionsBindResult.setStorehousePositionsResult(storehousePositionResult);
                }
            }
        }

    }

    @Override
    public List<StorehousePositionsResult> sku2position(Long skuId) {
        List<StorehousePositionsBind> positionsBinds = this.query().eq("sku_id", skuId).eq("display", 1).list();

        List<Storehouse> storehouseList = storehouseService.list();

        List<Long> positionIds = new ArrayList<>();
        for (StorehousePositionsBind positionsBind : positionsBinds) {
            positionIds.add(positionsBind.getPositionId());
        }
        List<StorehousePositionsDeptBindResult> bindByPositionIds = positionIds.size() > 0 ? storehousePositionsDeptBindService.getBindByPositionIds(positionIds) : new ArrayList<>();
        List<StorehousePositionsResult> results = new ArrayList<>();

        positionIds.clear();
        Long deptId = LoginContextHolder.getContext().getUser().getDeptId();
        for (StorehousePositionsDeptBindResult bindByPositionId : bindByPositionIds) {
            if (bindByPositionId.getDeptIds().stream().anyMatch(i -> i.equals(deptId))) {
                positionIds.add(bindByPositionId.getStorehousePositionsId());
            }
        }
        List<StorehousePositions> storehousePositions = positionIds.size() > 0 ? positionsService.listByIds(positionIds) : new ArrayList<>();
        for (StorehousePositions storehousePosition : storehousePositions) {
            StorehousePositionsResult storehousePositionsResult = new StorehousePositionsResult();
            ToolUtil.copyProperties(storehousePosition, storehousePositionsResult);
            for (Storehouse storehouse : storehouseList) {
                if (storehouse.getStorehouseId().equals(storehousePosition.getStorehouseId())) {
                    StorehouseResult storehouseResult = new StorehouseResult();
                    ToolUtil.copyProperties(storehouse, storehouseResult);
                    storehousePositionsResult.setStorehouseResult(storehouseResult);
                }
            }
            results.add(storehousePositionsResult);
        }

        return results;
    }


    private Serializable getKey(StorehousePositionsBindParam param) {
        return param.getBindId();
    }

    private Page<StorehousePositionsBindResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private StorehousePositionsBind getOldEntity(StorehousePositionsBindParam param) {
        return this.getById(getKey(param));
    }

    private StorehousePositionsBind getEntity(StorehousePositionsBindParam param) {
        StorehousePositionsBind entity = new StorehousePositionsBind();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }


}
