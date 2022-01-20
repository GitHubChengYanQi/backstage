package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.BusinessTrackParam;
import cn.atsoft.dasheng.app.model.params.StockDetailsParam;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Data;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.mapper.InstockOrderMapper;
import cn.atsoft.dasheng.erp.model.params.InstockOrderParam;
import cn.atsoft.dasheng.erp.model.request.InstockParams;
import cn.atsoft.dasheng.erp.model.result.BackSku;
import cn.atsoft.dasheng.erp.model.result.InstockListResult;
import cn.atsoft.dasheng.erp.model.result.InstockOrderResult;
import cn.atsoft.dasheng.erp.model.result.InstockRequest;
import cn.atsoft.dasheng.erp.pojo.FreeInStockParam;
import cn.atsoft.dasheng.erp.pojo.InstockListRequest;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.orCode.entity.OrCode;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.model.result.BackCodeRequest;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.orCode.service.OrCodeService;
import cn.atsoft.dasheng.portal.repair.service.RepairSendTemplate;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sendTemplate.WxCpTemplate;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.*;

/**
 * <p>
 * 入库单 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-06
 */
@Service
public class InstockOrderServiceImpl extends ServiceImpl<InstockOrderMapper, InstockOrder> implements InstockOrderService {
    @Autowired
    private InstockService instockService;
    @Autowired
    private UserService userService;
    @Autowired
    private StorehouseService storehouseService;
    @Autowired
    private InstockListService instockListService;
    @Autowired
    private InstockSendTemplate instockSendTemplate;
    @Autowired
    private OrCodeService orCodeService;
    @Autowired
    private CodingRulesService codingRulesService;
    @Autowired
    private InkindService inkindService;
    @Autowired
    private StockService stockService;
    @Autowired
    private StockDetailsService stockDetailsService;
    @Autowired
    private OrCodeBindService bindService;
    @Autowired
    private WxCpSendTemplate wxCpSendTemplate;
    @Autowired
    private StorehousePositionsService positionsService;

    @Override
    @Transactional
    public void add(InstockOrderParam param) {

        CodingRules codingRules = codingRulesService.query().eq("coding_rules_id", param.getCoding()).one();
        if (ToolUtil.isNotEmpty(codingRules)) {
            String backCoding = codingRulesService.backCoding(codingRules.getCodingRulesId());
            Storehouse storehouse = storehouseService.query().eq("storehouse_id", param.getStoreHouseId()).one();
            if (ToolUtil.isNotEmpty(storehouse)) {
                String replace = "";
                if (ToolUtil.isNotEmpty(storehouse.getCoding())) {
                    replace = backCoding.replace("${storehouse}", storehouse.getCoding());
                } else {
                    replace = backCoding.replace("${storehouse}", "");
                }
                param.setCoding(replace);
            }
        }
        //防止添加重复数据
        List<Long> judge = new ArrayList<>();
        for (InstockRequest instockRequest : param.getInstockRequest()) {
            Long brandId = instockRequest.getBrandId();
            Long skuId = instockRequest.getSkuId();
            judge.add(brandId + skuId);
        }
        long count = judge.stream().distinct().count();
        if (param.getInstockRequest().size() > count) {
            throw new ServiceException(500, "请勿重复添加");
        }
        InstockOrder entity = getEntity(param);
        this.save(entity);
        if (ToolUtil.isNotEmpty(param.getInstockRequest())) {
            List<InstockList> instockLists = new ArrayList<>();
            for (InstockRequest instockRequest : param.getInstockRequest()) {
                if (ToolUtil.isNotEmpty(instockRequest)) {
                    InstockList instockList = new InstockList();
                    instockList.setSkuId(instockRequest.getSkuId());
                    instockList.setNumber(instockRequest.getNumber());
                    instockList.setInstockOrderId(entity.getInstockOrderId());
                    instockList.setInstockNumber(instockRequest.getNumber());
                    instockList.setBrandId(instockRequest.getBrandId());
                    if (ToolUtil.isNotEmpty(instockRequest.getCostprice())) {
                        instockList.setCostPrice(instockRequest.getCostprice());
                    }
                    instockList.setStoreHouseId(param.getStoreHouseId());
                    if (ToolUtil.isNotEmpty(instockRequest.getSellingPrice())) {
                        instockList.setSellingPrice(instockRequest.getSellingPrice());
                    }
                    instockLists.add(instockList);
                }
            }
            if (ToolUtil.isNotEmpty(instockLists)) {
                instockListService.saveBatch(instockLists);
            }


            BackCodeRequest backCodeRequest = new BackCodeRequest();
            backCodeRequest.setId(entity.getInstockOrderId());
            backCodeRequest.setSource("instock");
            Long aLong = orCodeService.backCode(backCodeRequest);


//            String url = param.getUrl().replace("codeId", aLong.toString());


            User createUser = userService.getById(entity.getCreateUser());
            //新微信推送
            WxCpTemplate wxCpTemplate = new WxCpTemplate();
//            wxCpTemplate.setUrl(url);
            wxCpTemplate.setTitle("新的入库提醒");
            wxCpTemplate.setDescription(createUser.getName() + "您有新的入库任务" + entity.getCoding());
            wxCpTemplate.setUserIds(new ArrayList<Long>() {{
                add(entity.getUserId());
            }});
            wxCpSendTemplate.setSource("入库");
            wxCpSendTemplate.setSourceId(aLong);
            wxCpTemplate.setType(0);
            wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
            wxCpSendTemplate.sendTemplate();
        }
    }

    @Override

    public void delete(InstockOrderParam param) {
        this.removeById(getKey(param));
    }

    /**
     * 自由入库
     *
     * @param param
     */
    @Override
    @Transactional
    public void update(InstockOrderParam param) {
    }

    @Override
    public InstockOrderResult findBySpec(InstockOrderParam param) {
        return null;
    }

    @Override
    public List<InstockOrderResult> findListBySpec(InstockOrderParam param) {
        return null;
    }

    @Override
    public PageInfo<InstockOrderResult> findPageBySpec(InstockOrderParam param) {
        Page<InstockOrderResult> pageContext = getPageContext();
        IPage<InstockOrderResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    /**
     * 通过质检创建入库单
     *
     * @param instockParams
     */
    @Override
    @Transactional
    public void addByQuality(InstockParams instockParams) {
        //生成入库编码
        String coding = backCoding(instockParams.getCoding(), instockParams.getStoreHouseId());
        //查询实物
        List<Inkind> inkinds = inkindService.query().in("inkind_id", instockParams.getInkinds()).list();
        //创建入库单
        InstockOrder instockOrder = new InstockOrder();
        instockOrder.setCoding(coding);
        instockOrder.setUserId(instockParams.getUserId());
        instockOrder.setStoreHouseId(instockParams.getStoreHouseId());
        this.save(instockOrder);
        //通过实物id查询sku和brand  创建入库清单
        List<InstockList> instockLists = new ArrayList<>();
        for (Inkind inkind : inkinds) {
            inkind.setSource("质检");
            inkind.setType("0");
            //创建入库清单
            InstockList instockList = new InstockList();
            instockList.setSkuId(inkind.getSkuId());
            instockList.setBrandId(inkind.getBrandId());
            instockList.setNumber(inkind.getNumber());
            instockList.setInstockNumber(inkind.getNumber());
            instockList.setInstockOrderId(instockOrder.getInstockOrderId());
            instockList.setStoreHouseId(instockParams.getStoreHouseId());
            instockLists.add(instockList);
        }
        instockListService.saveBatch(instockLists);
        inkindService.updateBatchById(inkinds);
        //推送消息
        BusinessTrack businessTrack = new BusinessTrack();
        businessTrack.setType("代办");
        businessTrack.setMessage("入库");
        businessTrack.setUserId(instockParams.getUserId());
        businessTrack.setNote("有物料需要入库");
        DateTime data = new DateTime();
        businessTrack.setTime(data);
        BackCodeRequest backCodeRequest = new BackCodeRequest();
        backCodeRequest.setId(instockOrder.getInstockOrderId());
        backCodeRequest.setSource("instock");
        Long aLong = orCodeService.backCode(backCodeRequest);
        String url = instockParams.getUrl().replace("codeId", aLong.toString());
        instockSendTemplate.setBusinessTrack(businessTrack);
        instockSendTemplate.setUrl(url);
        instockSendTemplate.sendTemplate();
    }

    /**
     * 自由入库
     *
     * @param freeInStockParam
     */
    @Override
    public void freeInstock(FreeInStockParam freeInStockParam) {

        //判断库位
        List<StorehousePositions> pid = positionsService.query().eq("pid", freeInStockParam.getPositionsId()).list();
        if (ToolUtil.isNotEmpty(pid)) {
            throw new ServiceException(500, "请选择最下级库位");
        }


        List<Inkind> inkinds = inkindService.query().in("inkind_id", freeInStockParam.getInkindIds()).eq("type", 0).list();


        List<OrCodeBind> orCodeBinds = bindService.query().in("form_id", new ArrayList<Long>() {{
            for (Inkind inkind : inkinds) {
                add(inkind.getInkindId());
            }
        }}).list();

        List<Long> brandIds = new ArrayList<>();
        List<Long> skuIds = new ArrayList<>();
        Map<Long, Long> inkindNumber = new HashMap<>();

        for (Inkind inkind : inkinds) {     //计算相同实物的数量
            brandIds.add(inkind.getBrandId());
            skuIds.add(inkind.getSkuId());

            Long number = inkindNumber.get(inkind.getInkindId());
            if (ToolUtil.isEmpty(number)) {
                inkindNumber.put(inkind.getInkindId(), inkind.getNumber());
            } else {
                inkindNumber.put(inkind.getInkindId(), number + inkind.getNumber());
            }
        }


        List<Stock> stockList = stockService.lambdaQuery().eq(Stock::getStorehouseId, freeInStockParam.getStoreHouseId())  //查询库存
                .eq(Stock::getSkuId, skuIds)
                .in(Stock::getBrandId, brandIds)
                .list();


        Map<Long, Long> map = new HashMap<>();  //实物 组合 二维码id
        for (OrCodeBind codeBind : orCodeBinds) {
            for (Long codeId : freeInStockParam.getInkindIds()) {
                if (codeBind.getFormId().equals(codeId)) {
                    map.put(codeBind.getFormId(), codeBind.getOrCodeId());
                    break;
                }
            }
        }

        List<StockDetails> stockDetailsList = new ArrayList<>();
        List<Long> stockIds = new ArrayList<>();

        for (Inkind inkind : inkinds) {
            Long stockId = null;
            Stock exist = judgeStockExist(inkind, stockList);
            if (ToolUtil.isNotEmpty(exist)) {
                stockId = exist.getStockId();
            } else {  //没有相同库存
                Stock newStock = new Stock();
                newStock.setInventory(inkindNumber.get(inkind.getInkindId()));
                newStock.setBrandId(inkind.getBrandId());
                newStock.setSkuId(inkind.getSkuId());
                newStock.setStorehouseId(freeInStockParam.getStoreHouseId());
                stockService.save(newStock);
                stockId = newStock.getStockId();
            }
            StockDetails stockDetails = new StockDetails();
            stockDetails.setStockId(stockId);
            stockDetails.setNumber(inkind.getNumber());
            stockDetails.setStorehousePositionsId(freeInStockParam.getPositionsId());
            Long codeId = map.get(inkind.getInkindId());
            stockDetails.setQrCodeId(codeId);
            stockDetails.setInkindId(inkind.getInkindId());
            stockDetails.setStorehouseId(freeInStockParam.getStoreHouseId());

            stockDetails.setBrandId(inkind.getBrandId());
            stockDetails.setSkuId(inkind.getSkuId());
            stockDetailsList.add(stockDetails);
            inkind.setType("1");

            stockIds.add(stockId);
        }

        stockDetailsService.saveBatch(stockDetailsList);
        inkindService.updateBatchById(inkinds);
        stockService.updateNumber(stockIds);
    }

    /**
     * 判断实物有无库存
     *
     * @param inkind
     * @param stocks
     * @return
     */
    private Stock judgeStockExist(Inkind inkind, List<Stock> stocks) {
        if (ToolUtil.isEmpty(stocks)) {
            return null;
        }

        for (Stock stock : stocks) {
            if (stock.getSkuId().equals(inkind.getSkuId()) && stock.getBrandId().equals(inkind.getBrandId())) {
                return stock;
            }
        }
        return null;
    }


    private Serializable getKey(InstockOrderParam param) {
        return param.getInstockOrderId();
    }

    private Page<InstockOrderResult> getPageContext() {
        List<String> fields = new ArrayList<>();
        fields.add("storeHouseId");
        fields.add("createTime");
        fields.add("userId");
        return PageFactory.defaultPage(fields);
    }

    /**
     * 入库生成编码
     *
     * @param coding
     * @return
     */
    String backCoding(Long coding, Long storeHouseId) {
        String cod = null;
        //添加编码
        CodingRules codingRules = codingRulesService.query().eq("coding_rules_id", coding).one();
        if (ToolUtil.isNotEmpty(codingRules)) {
            String backCoding = codingRulesService.backCoding(codingRules.getCodingRulesId());
            Storehouse storehouse = storehouseService.query().eq("storehouse_id", storeHouseId).one();
            if (ToolUtil.isNotEmpty(storehouse)) {
                String replace = "";
                if (ToolUtil.isNotEmpty(storehouse.getCoding())) {
                    replace = backCoding.replace("${storehouse}", storehouse.getCoding());
                } else {
                    replace = backCoding.replace("${storehouse}", "");
                }
                cod = replace;
            }
        }
        return cod;
    }

    private InstockOrder getOldEntity(InstockOrderParam param) {
        return this.getById(getKey(param));
    }

    private InstockOrder getEntity(InstockOrderParam param) {
        InstockOrder entity = new InstockOrder();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    private void format(List<InstockOrderResult> data) {
        List<Long> userIds = new ArrayList<>();
        List<Long> storeIds = new ArrayList<>();

        for (InstockOrderResult datum : data) {
            userIds.add(datum.getUserId());
            storeIds.add(datum.getStoreHouseId());

        }
        List<User> users = userIds.size() == 0 ? new ArrayList<>() : userService.lambdaQuery().in(User::getUserId, userIds).list();
        List<Storehouse> storehouses = storeIds.size() == 0 ? new ArrayList<>() : storehouseService.lambdaQuery().in(Storehouse::getStorehouseId, storeIds).list();


        for (InstockOrderResult datum : data) {
            for (User user : users) {
                if (ToolUtil.isNotEmpty(datum.getUserId())) {
                    if (datum.getUserId().equals(user.getUserId())) {
                        UserResult userResult = new UserResult();
                        ToolUtil.copyProperties(user, userResult);
                        datum.setUserResult(userResult);
                        break;
                    }
                }

            }
            for (Storehouse storehouse : storehouses) {
                if (storehouse.getStorehouseId().equals(datum.getStoreHouseId())) {
                    StorehouseResult storehouseResult = new StorehouseResult();
                    ToolUtil.copyProperties(storehouse, storehouseResult);
                    datum.setStorehouseResult(storehouseResult);
                    break;
                }
            }
        }
    }
}
