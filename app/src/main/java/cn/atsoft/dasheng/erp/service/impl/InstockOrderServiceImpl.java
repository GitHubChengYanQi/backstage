package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.Instock;
import cn.atsoft.dasheng.app.service.InstockService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.InstockOrder;
import cn.atsoft.dasheng.erp.mapper.InstockOrderMapper;
import cn.atsoft.dasheng.erp.model.params.InstockOrderParam;
import cn.atsoft.dasheng.erp.model.result.InstockOrderResult;
import cn.atsoft.dasheng.erp.model.result.InstockRequest;
import cn.atsoft.dasheng.erp.service.InstockOrderService;
import cn.atsoft.dasheng.core.util.ToolUtil;
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

    @Override
    public void add(InstockOrderParam param) {
        InstockOrder entity = getEntity(param);
        this.save(entity);

        if (ToolUtil.isNotEmpty(param.getInstockRequest())) {
            List<Instock> instocks = new ArrayList<>();
            for (InstockRequest instockRequest : param.getInstockRequest()) {

                if (ToolUtil.isNotEmpty(instockRequest)) {

                    for (Long i = 0L; i < instockRequest.getNumber(); i++) {
                        Instock instock = new Instock();
                        instock.setBrandId(instockRequest.getBrandId());
                        instock.setItemId(instockRequest.getItemId());
                        instock.setStoreHouseId(param.getStoreHouseId());
                        instock.setInstockOrderId(entity.getInstockOrderId());
                        instocks.add(instock);
                    }
                }

            }
            if (ToolUtil.isNotEmpty(instocks)) {
                instockService.saveBatch(instocks);
            }

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
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(InstockOrderParam param) {
        return param.getInstockOrderId();
    }

    private Page<InstockOrderResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private InstockOrder getOldEntity(InstockOrderParam param) {
        return this.getById(getKey(param));
    }

    private InstockOrder getEntity(InstockOrderParam param) {
        InstockOrder entity = new InstockOrder();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
