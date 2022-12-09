package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.entity.SpuClassification;
import cn.atsoft.dasheng.erp.entity.StockForewarn;
import cn.atsoft.dasheng.erp.entity.StorehousePositions;
import cn.atsoft.dasheng.erp.mapper.StockForewarnMapper;
import cn.atsoft.dasheng.erp.model.params.StockForewarnParam;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.SpuClassificationService;
import cn.atsoft.dasheng.erp.service.StockForewarnService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
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
 * 库存预警设置 服务实现类
 * </p>
 *
 * @author sjl
 * @since 2022-12-05
 */
@Service
public class StockForewarnServiceImpl extends ServiceImpl<StockForewarnMapper, StockForewarn> implements StockForewarnService {

    @Autowired
    private StockForewarnService stockForewarnService;

    @Autowired
    private SkuService skuService;

    @Autowired
    private SpuClassificationService spuClassificationService;

    @Autowired
    private StorehousePositionsService storehousePositionsService;

    @Autowired
    private UserService userService;

    @Override
    public void add(StockForewarnParam param) {
        if (ToolUtil.isEmpty(param.getFormId())) {
            throw new ServiceException(500, "内容不能为空");
        } else {
            param.setFormId(param.getFormId());
        }
        /**
         * 强制的，和非强制的
         * 是否已设置过呢？
         */
        if (ToolUtil.isEmpty(param.getUpdate())) {
            /**
             * 非强制的
             */
            Integer count = this.query().eq("form_id", param.getFormId()).count();
            if (count > 0){
                throw new ServiceException(1001,"此条件已设置，是否更新预警条件");
            }
        }
        /**
         * 如果强制更新，先进行删除再进行保存
         * 那么怎么删除之前的数据呢？？？
         */
        List<StockForewarn> stockForewarn = this.query().eq("type", param.getType()).eq("form_id", param.getFormId()).list();
        List<Long> stockForewarnIds = new ArrayList<>();
        for (StockForewarn forewarn : stockForewarn) {
            stockForewarnIds.add(forewarn.getForewarnId());
        }
        if (ToolUtil.isNotEmpty(stockForewarnIds)) {
            this.removeByIds(stockForewarnIds);
        }
        StockForewarn entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(StockForewarnParam param) {
        if (ToolUtil.isEmpty(param.getForewarnId())) {
            throw new ServiceException(500,"删除目标不存在");
        }else {
            param.setDisplay(0);
            this.update(param);
        }
    }

    @Override
    public void update(StockForewarnParam param) {
        StockForewarn oldEntity = getOldEntity(param);
        StockForewarn newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public StockForewarnResult findBySpec(StockForewarnParam param) {
        return null;
    }

    @Override
    public List<StockForewarnResult> findListBySpec(StockForewarnParam param) {
        return null;
    }

    @Override
    public PageInfo<StockForewarnResult> findPageBySpec(StockForewarnParam param) {
        Page<StockForewarnResult> pageContext = getPageContext();
        IPage<StockForewarnResult> page = this.baseMapper.customPageList(pageContext, param);
        this.format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(StockForewarnParam param) {
        return param.getForewarnId();
    }

    private Page<StockForewarnResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private StockForewarn getOldEntity(StockForewarnParam param) {
        return this.getById(getKey(param));
    }

    private StockForewarn getEntity(StockForewarnParam param) {
        StockForewarn entity = new StockForewarn();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    public void format(List<StockForewarnResult> data) {
        List<Long> skuIds = new ArrayList<>();
        List<Long> classificationIds = new ArrayList<>();
        List<Long> positionIds = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        for (StockForewarnResult stockForewarnResult : data) {
            skuIds.add(stockForewarnResult.getFormId());
            classificationIds.add(stockForewarnResult.getFormId());
            positionIds.add(stockForewarnResult.getFormId());
            userIds.add(stockForewarnResult.getCreateUser());
        }
        List<SkuSimpleResult> skuResults  = skuIds.size() == 0 ? new ArrayList<>() : skuService.simpleFormatSkuResult(skuIds);

        List<SpuClassification> spuClassificationList = classificationIds.size() == 0 ? new ArrayList<>() : spuClassificationService.listByIds(classificationIds);
        List<SpuClassificationResult> classificationResults = BeanUtil.copyToList(spuClassificationList, SpuClassificationResult.class, new CopyOptions());

        List<StorehousePositions> storehousePositionsList = positionIds.size() == 0 ? new ArrayList<>() : storehousePositionsService.listByIds(positionIds);
        List<StorehousePositionsResult> positionsResults = BeanUtil.copyToList(storehousePositionsList, StorehousePositionsResult.class, new CopyOptions());

        List<UserResult> userList = userIds.size() == 0 ? new ArrayList<>() : userService.getUserResultsByIds(userIds);


        for (StockForewarnResult forewarnResult : data) {
            if (ToolUtil.isNotEmpty(skuIds)) {
                for (SkuSimpleResult skuResult : skuResults) {
                    if (forewarnResult.getFormId().equals(skuResult.getSkuId())) {
                        forewarnResult.setSkuResult(skuResult);
                        break;
                    }
                }
            }
            if (ToolUtil.isNotEmpty(classificationIds)) {
                for (SpuClassificationResult spuClassificationResult : classificationResults) {
                    if (forewarnResult.getFormId().equals(spuClassificationResult.getSpuClassificationId())) {
                        forewarnResult.setSpuClassificationResult(spuClassificationResult);
                        break;
                    }
                }
            }
            if (ToolUtil.isNotEmpty(positionIds)) {
                for (StorehousePositionsResult storehousePositionsResult : positionsResults) {
                    if (forewarnResult.getFormId().equals(storehousePositionsResult.getStorehousePositionsId())) {
                        forewarnResult.setStorehousePositionsResult(storehousePositionsResult);
                        break;
                    }
                }
            }
            if (ToolUtil.isNotEmpty(userIds)) {
                for (UserResult userResult : userList) {
                    forewarnResult.setCreateUserResult(userResult);
                    break;
                }
            }
        }
    }

}
