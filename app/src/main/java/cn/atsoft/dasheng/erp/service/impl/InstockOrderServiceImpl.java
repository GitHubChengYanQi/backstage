package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.BusinessTrack;
import cn.atsoft.dasheng.app.entity.Instock;
import cn.atsoft.dasheng.app.entity.Storehouse;
import cn.atsoft.dasheng.app.model.params.BusinessTrackParam;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.app.service.BusinessTrackService;
import cn.atsoft.dasheng.app.service.InstockService;
import cn.atsoft.dasheng.app.service.StorehouseService;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Data;
import cn.atsoft.dasheng.erp.entity.CodingRules;
import cn.atsoft.dasheng.erp.entity.InstockList;
import cn.atsoft.dasheng.erp.entity.InstockOrder;
import cn.atsoft.dasheng.erp.entity.SpuClassification;
import cn.atsoft.dasheng.erp.mapper.InstockOrderMapper;
import cn.atsoft.dasheng.erp.model.params.InstockOrderParam;
import cn.atsoft.dasheng.erp.model.result.BackSku;
import cn.atsoft.dasheng.erp.model.result.InstockOrderResult;
import cn.atsoft.dasheng.erp.model.result.InstockRequest;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.orCode.model.result.BackCodeRequest;
import cn.atsoft.dasheng.orCode.service.OrCodeService;
import cn.atsoft.dasheng.portal.repair.service.RepairSendTemplate;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private SkuService skuService;
    @Autowired
    private BusinessTrackService businessTrackService;
    @Autowired
    private InstockSendTemplate instockSendTemplate;
    @Autowired
    private RepairSendTemplate repairSendTemplate;
    @Autowired
    private OrCodeService orCodeService;
    @Autowired
    private CodingRulesService codingRulesService;

    @Override
    @Transactional
    public void add(InstockOrderParam param) {

        CodingRules codingRules = codingRulesService.query().eq("coding_rules_id", param.getCoding()).one();
        if (ToolUtil.isNotEmpty(codingRules)) {
            String backCoding = codingRulesService.backCoding(codingRules.getCodingRulesId());
            Storehouse storehouse = storehouseService.query().eq("storehouse_id", param.getStoreHouseId()).one();
            if (ToolUtil.isNotEmpty(storehouse)) {
                String replace = backCoding.replace("${storehouse}", storehouse.getCoding());
                param.setCoding(replace);
            }
        }
        //防止添加重复数据
        List<Long> judge = new ArrayList<>();
        for (InstockRequest instockRequest : param.getInstockRequest()) {
            Long brandId = instockRequest.getBrandId();
            Long skuId = instockRequest.getSkuId();
            Integer sellingPrice = instockRequest.getSellingPrice();
            Integer costprice = instockRequest.getCostprice();
            judge.add(brandId + skuId + sellingPrice + costprice);
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
                    instockList.setBrandId(instockRequest.getBrandId());
                    instockList.setStoreHouseId(param.getStoreHouseId());
                    instockList.setCostPrice(instockRequest.getCostprice());
                    instockList.setSellingPrice(instockRequest.getSellingPrice());
                    instockLists.add(instockList);
                }
            }
            if (ToolUtil.isNotEmpty(instockLists)) {
                instockListService.saveBatch(instockLists);
            }
            //添加代办信息
            BusinessTrack businessTrack = new BusinessTrack();
            businessTrack.setType("代办");
            businessTrack.setMessage("入库");
            businessTrack.setUserId(param.getUserId());
            businessTrack.setNote("有物料需要入库");
            DateTime data = new DateTime();
            businessTrack.setTime(data);
            BackCodeRequest backCodeRequest = new BackCodeRequest();
            backCodeRequest.setId(entity.getInstockOrderId());
            backCodeRequest.setSource("instock");
            Long aLong = orCodeService.backCode(backCodeRequest);
            String url = param.getUrl().replace("codeId", aLong.toString());

            instockSendTemplate.setBusinessTrack(businessTrack);
            instockSendTemplate.setUrl(url);
            instockSendTemplate.sendTemolate();
            businessTrackService.save(businessTrack);
        }
    }

    @Override

    public void delete(InstockOrderParam param) {
        this.removeById(getKey(param));
    }

    @Override

    public void update(InstockOrderParam param) {
        InstockOrder oldEntity = getOldEntity(param);
        InstockOrder newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
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
        List<Long> skuIds = new ArrayList<>();
        for (InstockOrderResult datum : data) {
            userIds.add(datum.getUserId());
            storeIds.add(datum.getStoreHouseId());
        }
        List<User> users = userIds.size() == 0 ? new ArrayList<>() : userService.lambdaQuery().in(User::getUserId, userIds).list();
        List<Storehouse> storehouses = storeIds.size() == 0 ? new ArrayList<>() : storehouseService.lambdaQuery().in(Storehouse::getStorehouseId, storeIds).list();

        for (InstockOrderResult datum : data) {

            for (User user : users) {
                if (datum.getUserId().equals(user.getUserId())) {
                    UserResult userResult = new UserResult();
                    ToolUtil.copyProperties(user, userResult);
                    datum.setUserResult(userResult);
                    break;
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
