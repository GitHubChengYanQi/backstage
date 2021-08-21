package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.DeliveryDetails;
import cn.atsoft.dasheng.app.entity.Outstock;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.model.params.StockDetailsParam;
import cn.atsoft.dasheng.app.model.result.OutstockRequest;
import cn.atsoft.dasheng.app.service.DeliveryDetailsService;
import cn.atsoft.dasheng.app.service.OutstockService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Delivery;
import cn.atsoft.dasheng.app.mapper.DeliveryMapper;
import cn.atsoft.dasheng.app.model.params.DeliveryParam;
import cn.atsoft.dasheng.app.model.result.DeliveryResult;
import cn.atsoft.dasheng.app.service.DeliveryService;
import cn.atsoft.dasheng.core.util.ToolUtil;
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
 * 发货表 服务实现类
 * </p>
 *
 * @author
 * @since 2021-08-20
 */
@Service
public class DeliveryServiceImpl extends ServiceImpl<DeliveryMapper, Delivery> implements DeliveryService {
    @Autowired
    private DeliveryDetailsService deliveryDetailsService;
    @Autowired
    private OutstockService outstockService;
    @Autowired
    private StockDetailsService stockDetailsService;
    private int number;

    @Override
    public Long add(DeliveryParam param) {
        Delivery entity = getEntity(param);
        this.save(entity);
        return entity.getDeliveryId();
    }

    @Override
    public void delete(DeliveryParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(DeliveryParam param) {
        Delivery oldEntity = getOldEntity(param);
        Delivery newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public DeliveryResult findBySpec(DeliveryParam param) {
        return null;
    }

    @Override
    public List<DeliveryResult> findListBySpec(DeliveryParam param) {
        return null;
    }

    @Override
    public PageInfo<DeliveryResult> findPageBySpec(DeliveryParam param) {
        Page<DeliveryResult> pageContext = getPageContext();
        IPage<DeliveryResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public void bulkShipment(OutstockRequest outstockRequest) {

        List<DeliveryDetails> deliveryDetails = new ArrayList<>();


        for (Long id : outstockRequest.getIds()) {
            QueryWrapper<Outstock> outstockQueryWrapper = new QueryWrapper<>();
            outstockQueryWrapper.in("outstock_id", id);
            List<Outstock> outstockList = outstockService.list(outstockQueryWrapper);
            for (Outstock outstock : outstockList) {
                number = Math.toIntExact(outstock.getNumber());

            }

        }
        for (int i = 0; i < number; i++) {
            DeliveryDetails details = new DeliveryDetails();
            details.setPhoneId(outstockRequest.getPhoneId());
            details.setCustomerId(outstockRequest.getCustomerId());
            details.setContactsId(outstockRequest.getContactsId());
            details.setAdressId(outstockRequest.getAdressId());
            deliveryDetails.add(details);
        }
        deliveryDetailsService.saveBatch(deliveryDetails);
    }

    private Serializable getKey(DeliveryParam param) {
        return param.getDeliveryId();
    }

    private Page<DeliveryResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Delivery getOldEntity(DeliveryParam param) {
        return this.getById(getKey(param));
    }

    private Delivery getEntity(DeliveryParam param) {
        Delivery entity = new Delivery();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
