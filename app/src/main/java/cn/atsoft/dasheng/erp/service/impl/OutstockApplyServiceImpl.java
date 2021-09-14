package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.model.params.OutstockOrderParam;
import cn.atsoft.dasheng.app.model.params.OutstockParam;
import cn.atsoft.dasheng.app.service.OutstockOrderService;
import cn.atsoft.dasheng.app.service.OutstockService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.OutstockApply;
import cn.atsoft.dasheng.erp.mapper.OutstockApplyMapper;
import cn.atsoft.dasheng.erp.model.params.OutstockApplyParam;
import cn.atsoft.dasheng.erp.model.result.OutstockApplyResult;
import cn.atsoft.dasheng.erp.service.OutstockApplyService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 出库申请 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-09-14
 */
@Service
public class OutstockApplyServiceImpl extends ServiceImpl<OutstockApplyMapper, OutstockApply> implements OutstockApplyService {
    @Autowired
    private OutstockOrderService outstockOrderService;
    @Autowired
    private OutstockService outstockService;


    @Override
    public void add(OutstockApplyParam param) {
        OutstockApply entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(OutstockApplyParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(OutstockApplyParam param) {
        OutstockApply oldEntity = getOldEntity(param);
        OutstockApply newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
        if (newEntity.getApplyState().equals(2)) {
            //添加发货单
            OutstockOrderParam outstockOrderParam = new OutstockOrderParam();
            outstockOrderParam.setOutstockApplyId(newEntity.getOutstockApplyId());
            outstockOrderService.add(outstockOrderParam);
            //添加出库单
            OutstockParam outstockParam = new OutstockParam();
            outstockParam.setNumber(newEntity.getNumber());
            outstockParam.setBrandId(newEntity.getBrandId());
            outstockParam.setItemId(newEntity.getItemId());
            outstockParam.setPrice(newEntity.getPrice());
            outstockService.add(outstockParam);

        }


    }

    @Override
    public OutstockApplyResult findBySpec(OutstockApplyParam param) {
        return null;
    }

    @Override
    public List<OutstockApplyResult> findListBySpec(OutstockApplyParam param) {
        return null;
    }

    @Override
    public PageInfo<OutstockApplyResult> findPageBySpec(OutstockApplyParam param) {
        Page<OutstockApplyResult> pageContext = getPageContext();
        IPage<OutstockApplyResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(OutstockApplyParam param) {
        return param.getOutstockApplyId();
    }

    private Page<OutstockApplyResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private OutstockApply getOldEntity(OutstockApplyParam param) {
        return this.getById(getKey(param));
    }

    private OutstockApply getEntity(OutstockApplyParam param) {
        OutstockApply entity = new OutstockApply();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
