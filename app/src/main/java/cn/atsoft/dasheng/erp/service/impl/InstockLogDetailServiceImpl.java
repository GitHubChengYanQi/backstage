package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.InstockList;
import cn.atsoft.dasheng.erp.entity.InstockLogDetail;
import cn.atsoft.dasheng.erp.mapper.InstockLogDetailMapper;
import cn.atsoft.dasheng.erp.model.params.InstockLogDetailParam;
import cn.atsoft.dasheng.erp.model.result.InstockLogDetailResult;
import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import cn.atsoft.dasheng.erp.service.InstockListService;
import cn.atsoft.dasheng.erp.service.InstockLogDetailService;
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
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-04-14
 */
@Service
public class InstockLogDetailServiceImpl extends ServiceImpl<InstockLogDetailMapper, InstockLogDetail> implements InstockLogDetailService {
    @Autowired
    private SkuService skuService;
    @Autowired
    private InstockListService instockListService;

    @Override
    public void add(InstockLogDetailParam param) {
        InstockLogDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(InstockLogDetailParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(InstockLogDetailParam param) {
        InstockLogDetail oldEntity = getOldEntity(param);
        InstockLogDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public InstockLogDetailResult findBySpec(InstockLogDetailParam param) {
        return null;
    }

    @Override
    public List<InstockLogDetailResult> findListBySpec(InstockLogDetailParam param) {
        return null;
    }

    @Override
    public PageInfo<InstockLogDetailResult> findPageBySpec(InstockLogDetailParam param) {
        Page<InstockLogDetailResult> pageContext = getPageContext();
        IPage<InstockLogDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(InstockLogDetailParam param) {
        return param.getInstockLogDetailId();
    }

    private Page<InstockLogDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private InstockLogDetail getOldEntity(InstockLogDetailParam param) {
        return this.getById(getKey(param));
    }

    private InstockLogDetail getEntity(InstockLogDetailParam param) {
        InstockLogDetail entity = new InstockLogDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }
    @Override
    public List<InstockLogDetailResult> resultsByLogIds(List<Long> logIds) {
        if (ToolUtil.isEmpty(logIds) || logIds.size() == 0) {
            return new ArrayList<>();
        }
        List<InstockLogDetail> instockLogDetails = this.query().in("instock_log_id", logIds).list();
        List<InstockLogDetailResult> results = new ArrayList<>();
        for (InstockLogDetail instockLogDetail : instockLogDetails) {
            InstockLogDetailResult result = new InstockLogDetailResult();
            ToolUtil.copyProperties(instockLogDetail,result);
            results.add(result);
        }
        this.format(results);
        return results;
    }

    private void format(List<InstockLogDetailResult> results){
        List<Long> skuIds  = new ArrayList<>();
        List<Long> instockOrderId = new ArrayList<>();
        for (InstockLogDetailResult result : results) {
            skuIds.add(result.getSkuId());
            instockOrderId.add(result.getInstockOrderId());
        }
        instockOrderId = instockOrderId.stream().distinct().collect(Collectors.toList());
        List<InstockList> instockLists =instockOrderId.size() == 0 ? new ArrayList<>() : instockListService.query().in("instock_order_id", instockOrderId).list();


        List<SkuSimpleResult> skuSimpleResults = skuService.simpleFormatSkuResult(skuIds);
        for (InstockLogDetailResult result : results) {
            for (SkuSimpleResult skuSimpleResult : skuSimpleResults) {
                if (result.getSkuId().equals(skuSimpleResult.getSkuId())){
                    result.setSkuResult(skuSimpleResult);
                }
            }
            for (InstockList instockList : instockLists) {
                if (instockList.getInstockOrderId().equals(result.getInstockOrderId()) && instockList.getSkuId().equals(result.getSkuId())){
                    result.setListNumber(Math.toIntExact(instockList.getNumber()));
                }
            }
        }

    }

}
