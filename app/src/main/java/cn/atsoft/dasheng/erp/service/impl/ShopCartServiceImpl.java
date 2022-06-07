package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.ShopCart;
import cn.atsoft.dasheng.erp.mapper.ShopCartMapper;
import cn.atsoft.dasheng.erp.model.params.ShopCartParam;
import cn.atsoft.dasheng.erp.model.result.ShopCartResult;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.ShopCartService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.SkuService;
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
 * 购物车 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-06-06
 */
@Service
public class ShopCartServiceImpl extends ServiceImpl<ShopCartMapper, ShopCart> implements ShopCartService {
    @Autowired
    private SkuService skuService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CustomerService customerService;


    @Override
    public Long add(ShopCartParam param) {
        ShopCart entity = getEntity(param);
        this.save(entity);
        return entity.getCartId();
    }

    @Override
    public void addList(List<ShopCartParam> params) {
        List<ShopCart> shopCarts = new ArrayList<>();
        for (ShopCartParam param : params) {
            ShopCart entity = getEntity(param);
            shopCarts.add(entity);
        }
        this.saveBatch(shopCarts);
    }


    @Override
    public List<Long> delete(ShopCartParam param) {

        if (ToolUtil.isNotEmpty(param.getIds()) && param.getIds().size() > 0) {
            List<ShopCart> shopCarts = this.listByIds(param.getIds());
            for (ShopCart shopCart : shopCarts) {
                shopCart.setDisplay(0);
            }
            this.updateBatchById(shopCarts);
            return param.getIds();
        }

//        ShopCart entity = getEntity(param);
//        entity.setDisplay(0);
//        this.updateById(entity);
        return null;
    }

    @Override
    public Long update(ShopCartParam param) {
        ShopCart oldEntity = getOldEntity(param);
        ShopCart newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
        return param.getCartId();
    }

    @Override
    public List<ShopCartResult> allList(ShopCartParam param) {
        param.setCreateUser(LoginContextHolder.getContext().getUserId());
        List<ShopCartResult> shopCartResults = this.baseMapper.customList(param);
        format(shopCartResults);
        return shopCartResults;
    }

    @Override
    public ShopCartResult findBySpec(ShopCartParam param) {
        return null;
    }

    @Override
    public List<ShopCartResult> findListBySpec(ShopCartParam param) {
        return null;
    }

    @Override
    public PageInfo<ShopCartResult> findPageBySpec(ShopCartParam param) {
        Page<ShopCartResult> pageContext = getPageContext();
        IPage<ShopCartResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ShopCartParam param) {
        return param.getCartId();
    }

    private Page<ShopCartResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ShopCart getOldEntity(ShopCartParam param) {
        return this.getById(getKey(param));
    }

    private ShopCart getEntity(ShopCartParam param) {
        ShopCart entity = new ShopCart();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    private ShopCartResult getResult(ShopCartParam param) {
        ShopCartResult result = new ShopCartResult();
        ToolUtil.copyProperties(param, result);
        format(new ArrayList<ShopCartResult>() {{
            add(result);
        }});
        return result;
    }

    private void format(List<ShopCartResult> data) {
        List<Long> skuIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        List<Long> customerIds = new ArrayList<>();

        for (ShopCartResult datum : data) {
            customerIds.add(datum.getCustomerId());
            brandIds.add(datum.getBrandId());
            skuIds.add(datum.getSkuId());
        }

        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
        List<BrandResult> brandResults = brandService.getBrandResults(brandIds);
        List<Customer> customerList = customerIds.size() == 0 ? new ArrayList<>() : customerService.listByIds(customerIds);


        for (ShopCartResult datum : data) {
            for (SkuResult skuResult : skuResults) {
                if (datum.getSkuId().equals(skuResult.getSkuId())) {
                    datum.setSkuResult(skuResult);
                    break;
                }
            }
            for (BrandResult brandResult : brandResults) {
                if (ToolUtil.isNotEmpty(datum.getBrandId()) && datum.getBrandId().equals(brandResult.getBrandId())) {
                    datum.setBrandResult(brandResult);
                    break;
                }
            }
            for (Customer customer : customerList) {
                if (ToolUtil.isNotEmpty(datum.getCustomerId()) && datum.getCustomerId().equals(customer.getCustomerId())) {
                    datum.setCustomer(customer);
                    break;
                }
            }
        }


    }


}
