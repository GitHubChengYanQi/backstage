package cn.atsoft.dasheng.purchase.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.purchase.entity.PurchaseAsk;
import cn.atsoft.dasheng.purchase.entity.PurchaseListing;
import cn.atsoft.dasheng.purchase.mapper.PurchaseAskMapper;
import cn.atsoft.dasheng.purchase.model.params.PurchaseAskParam;
import cn.atsoft.dasheng.purchase.model.params.PurchaseListingParam;
import cn.atsoft.dasheng.purchase.model.result.PurchaseAskResult;
import cn.atsoft.dasheng.purchase.model.result.PurchaseListingResult;
import  cn.atsoft.dasheng.purchase.service.PurchaseAskService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.purchase.service.PurchaseListingService;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 采购申请 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-12-15
 */
@Service
public class PurchaseAskServiceImpl extends ServiceImpl<PurchaseAskMapper, PurchaseAsk> implements PurchaseAskService {
    @Autowired
    private UserService userService;
    @Autowired
    private PurchaseListingService purchaseListingService;
    @Override
    @Transactional
    public void add(PurchaseAskParam param){
        PurchaseAsk entity = getEntity(param);
        this.save(entity);
        List<PurchaseListing> purchaseListings = new ArrayList<>();
        for (PurchaseListingParam purchaseListingParam : param.getPurchaseListingParams()) {
            purchaseListingParam.setPurchaseAskId(entity.getPurchaseAskId());
            PurchaseListing purchaseListing = new PurchaseListing();
            ToolUtil.copyProperties(purchaseListingParam,purchaseListing);
            purchaseListings.add(purchaseListing);
        }
        purchaseListingService.saveBatch(purchaseListings);
    }

//    @Override
//    public void delete(PurchaseAskParam param){
//        this.removeById(getKey(param));
//    }

    @Override
    public void update(PurchaseAskParam param){
        PurchaseAsk oldEntity = getOldEntity(param);
        PurchaseAsk newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public PurchaseAskResult findBySpec(PurchaseAskParam param){
        return null;
    }

    @Override
    public List<PurchaseAskResult> findListBySpec(PurchaseAskParam param){
        return null;
    }

    @Override
    public PageInfo<PurchaseAskResult> findPageBySpec(PurchaseAskParam param){
        Page<PurchaseAskResult> pageContext = getPageContext();
        IPage<PurchaseAskResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    public void format(List<PurchaseAskResult> param){
        List<Long> userIds = new ArrayList<>();
        for (PurchaseAskResult purchaseAskResult : param) {
            userIds.add(purchaseAskResult.getCreateUser());
        }
        List<User> userList = userService.listByIds(userIds);
        for (PurchaseAskResult purchaseAskResult : param) {
            for (User user : userList) {
                if (purchaseAskResult.getCreateUser().equals(user.getUserId())) {
                    purchaseAskResult.setCreateUserName(user.getName());
                }
            }
        }
    }
    @Override
    public PurchaseAskResult detail(PurchaseAskParam param){
        PurchaseAsk detail = this.getById(param.getPurchaseAskId());
        PurchaseAskResult result = new PurchaseAskResult();
        ToolUtil.copyProperties(detail, result);


        List<PurchaseListingResult> purchaseListing = purchaseListingService.getByAskId(param.getPurchaseAskId());
        result.setPurchaseListingResults(purchaseListing);

        return result;
    }
    private Serializable getKey(PurchaseAskParam param){
        return param.getPurchaseAskId();
    }

    private Page<PurchaseAskResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private PurchaseAsk getOldEntity(PurchaseAskParam param) {
        return this.getById(getKey(param));
    }

    private PurchaseAsk getEntity(PurchaseAskParam param) {
        PurchaseAsk entity = new PurchaseAsk();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
