package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.model.result.StockDetailsResult;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.mapper.StockForewarnMapper;
import cn.atsoft.dasheng.erp.model.params.StockForewarnParam;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
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
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private StockDetailsService stockDetailsService;
    @Autowired
    private InstockListService instockListService;
    @Autowired
    private InstockOrderService instockOrderService;

    @Override
    public void add(StockForewarnParam param) {
        if (ToolUtil.isEmpty(param.getFormId())) {
            throw new ServiceException(500, "内容不能为空");
        } else {
            param.setFormId(param.getFormId());
        }
        //直接覆盖以前的数据
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
            throw new ServiceException(500, "删除目标不存在");
        } else {
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
        List<SkuSimpleResult> skuResults = skuIds.size() == 0 ? new ArrayList<>() : skuService.simpleFormatSkuResult(skuIds);
       
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

    @Override
    public PageInfo showWaring(StockForewarnParam param) {
        Page<StockForewarnResult> pageContext = this.getPageContext();
        Page<StockForewarnResult> stockForewarnResultPage = this.baseMapper.warningSkuPageList(pageContext, param);
        List<Long> skuIds = stockForewarnResultPage.getRecords().stream().map(StockForewarnResult::getSkuId).distinct().collect(Collectors.toList());
        List<InstockList> instockLists = skuIds.size() == 0 ? new ArrayList<>() : instockListService.lambdaQuery().eq(InstockList::getStatus, 0).eq(InstockList::getDisplay, 1).in(InstockList::getSkuId, skuIds).list();
        /**
         * 数据组合
         */
        List<InstockList> totalList = new ArrayList<>();
        instockLists.parallelStream().collect(Collectors.groupingBy(InstockList::getSkuId, Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new InstockList() {{
                        ToolUtil.copyProperties(a,this);
                        setNumber(a.getNumber()+b.getNumber());
                        setInstockNumber(a.getInstockNumber()+b.getInstockNumber());
                    }}).ifPresent(totalList::add);
                }
        );
       
        List<SkuSimpleResult> skuResult =stockForewarnResultPage.getRecords().size() == 0 ? new ArrayList<>() : skuService.simpleFormatSkuResult(stockForewarnResultPage.getRecords().stream().map(StockForewarnResult::getSkuId).distinct().collect(Collectors.toList()));
        for (StockForewarnResult record : stockForewarnResultPage.getRecords()) {
            for (SkuSimpleResult skuSimpleResult : skuResult) {
                if (record.getSkuId().equals(skuSimpleResult.getSkuId())){
                    record.setSkuResult(skuSimpleResult);
                    break;
                }
            }
            record.setFloatingCargoNumber(0L);
            for (InstockList instockList : totalList) {
                if(instockList.getSkuId().equals(record.getSkuId())){
                    record.setFloatingCargoNumber(instockList.getNumber()-instockList.getInstockNumber());
                    break;
                }
            }
        }



        return PageFactory.createPageInfo(stockForewarnResultPage);

    }

    @Override
    public List<StockForewarnResult> listBySkuIds(List<Long> skuIds){
        if (ToolUtil.isEmpty(skuIds) || skuIds.size()==0){
            return new ArrayList<>();
        }
        return this.baseMapper.warningSkuList(new StockForewarnParam(){{
            setSkuIds(skuIds);
        }});

        
    }


}
