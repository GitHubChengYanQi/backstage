package cn.atsoft.dasheng.inventory.service.impl;


import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.model.result.InkindResult;
import cn.atsoft.dasheng.erp.service.InkindService;
import cn.atsoft.dasheng.inventory.entity.InventoryDetail;
import cn.atsoft.dasheng.inventory.mapper.InventoryDetailMapper;
import cn.atsoft.dasheng.inventory.model.params.InventoryDetailParam;
import cn.atsoft.dasheng.inventory.model.result.InventoryDetailResult;
import cn.atsoft.dasheng.inventory.service.InventoryDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
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
 * 盘点任务详情 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-27
 */
@Service
public class InventoryDetailServiceImpl extends ServiceImpl<InventoryDetailMapper, InventoryDetail> implements InventoryDetailService {

    @Autowired
    private InkindService inkindService;
    @Autowired
    private StockDetailsService stockDetailsService;

    @Override
    public void add(InventoryDetailParam param) {
        if (param.getStatus() == 0) {  //正常
            StockDetails inkindId = stockDetailsService.query().eq("inkind_id", param.getInkindId()).one();
            if (ToolUtil.isEmpty(inkindId)) {
                throw new ServiceException(500, "当前物料在库存中不存在");
            }
            if (inkindId.getNumber() < 0) {
                throw new ServiceException(500, "当前物流已经出库,此物料异常");
            }
            InventoryDetail entity = getEntity(param);
            this.save(entity);
        }

    }

    @Override
    public void delete(InventoryDetailParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(InventoryDetailParam param) {
        InventoryDetail oldEntity = getOldEntity(param);
        InventoryDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public InventoryDetailResult findBySpec(InventoryDetailParam param) {
        return null;
    }

    @Override
    public List<InventoryDetailResult> findListBySpec(InventoryDetailParam param) {
        return null;
    }

    @Override
    public PageInfo<InventoryDetailResult> findPageBySpec(InventoryDetailParam param) {
        Page<InventoryDetailResult> pageContext = getPageContext();
        IPage<InventoryDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(InventoryDetailParam param) {
        return param.getDetailId();
    }

    private Page<InventoryDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private InventoryDetail getOldEntity(InventoryDetailParam param) {
        return this.getById(getKey(param));
    }

    private InventoryDetail getEntity(InventoryDetailParam param) {
        InventoryDetail entity = new InventoryDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    private void format(List<InventoryDetailResult> data) {
        List<Long> inkindIds = new ArrayList<>();
        for (InventoryDetailResult datum : data) {
            inkindIds.add(datum.getInkindId());
        }
        List<InkindResult> inKinds = inkindService.getInKinds(inkindIds);

        for (InventoryDetailResult datum : data) {
            for (InkindResult inKind : inKinds) {
                if (datum.getInkindId().equals(inKind.getInkindId())) {
                    datum.setInkindResult(inKind);
                    break;
                }
            }
        }
    }
}
