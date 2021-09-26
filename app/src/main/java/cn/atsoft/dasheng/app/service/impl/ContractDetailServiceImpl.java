package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.Items;
import cn.atsoft.dasheng.app.model.result.CrmBusinessDetailedResult;
import cn.atsoft.dasheng.app.model.result.ItemsResult;
import cn.atsoft.dasheng.app.service.ItemsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.ContractDetail;
import cn.atsoft.dasheng.app.mapper.ContractDetailMapper;
import cn.atsoft.dasheng.app.model.params.ContractDetailParam;
import cn.atsoft.dasheng.app.model.result.ContractDetailResult;
import  cn.atsoft.dasheng.app.service.ContractDetailService;
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
 * 合同产品明细 服务实现类
 * </p>
 *
 * @author sb
 * @since 2021-09-18
 */
@Service
public class ContractDetailServiceImpl extends ServiceImpl<ContractDetailMapper, ContractDetail> implements ContractDetailService {


    @Autowired
    private ItemsService itemsService;

    @Override
    public void add(ContractDetailParam param){

        ContractDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ContractDetailParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ContractDetailParam param){
        ContractDetail oldEntity = getOldEntity(param);
        ContractDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ContractDetailResult findBySpec(ContractDetailParam param){
        return null;
    }

    @Override
    public List<ContractDetailResult> findListBySpec(ContractDetailParam param){
        return null;
    }

    @Override
    public PageInfo<ContractDetailResult> findPageBySpec(ContractDetailParam param){
        Page<ContractDetailResult> pageContext = getPageContext();
        IPage<ContractDetailResult> page = this.baseMapper.customPageList(pageContext, param);


        List<Long> detailIds = new ArrayList<>();
        for (ContractDetailResult record : page.getRecords()) {
            detailIds.add(record.getItemId());
        }
        QueryWrapper<Items> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("item_id", detailIds);
        List<Items> list = detailIds.size() == 0 ? new ArrayList<>() : itemsService.list(queryWrapper);
        for (ContractDetailResult record : page.getRecords()) {
            for (Items items : list) {
                if (items.getItemId().equals(record.getItemId())) {
                    ItemsResult itemsResult = new ItemsResult();
                    ToolUtil.copyProperties(items, itemsResult);
                    record.setItemsResult(itemsResult);
                    break;
                }
            }
        }

        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ContractDetailParam param){
        return param.getId();
    }

    private Page<ContractDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ContractDetail getOldEntity(ContractDetailParam param) {
        return this.getById(getKey(param));
    }

    private ContractDetail getEntity(ContractDetailParam param) {
        ContractDetail entity = new ContractDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
