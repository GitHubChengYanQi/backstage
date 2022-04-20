package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.ContractTemplete;
import cn.atsoft.dasheng.crm.entity.ContractTempleteDetail;
import cn.atsoft.dasheng.crm.mapper.ContractTempleteMapper;
import cn.atsoft.dasheng.crm.model.params.ContractTempleteDetailParam;
import cn.atsoft.dasheng.crm.model.params.ContractTempleteParam;
import cn.atsoft.dasheng.crm.model.result.ContractTempleteDetailResult;
import cn.atsoft.dasheng.crm.model.result.ContractTempleteResult;
import cn.atsoft.dasheng.crm.service.ContractTempleteDetailService;
import cn.atsoft.dasheng.crm.service.ContractTempleteService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.Tool;
import cn.atsoft.dasheng.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.Query;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 自定义合同变量 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-04-18
 */
@Service
public class ContractTempleteServiceImpl extends ServiceImpl<ContractTempleteMapper, ContractTemplete> implements ContractTempleteService {

    @Autowired
    private ContractTempleteDetailService contractTempleteDetailService;

    private void checkName(String name) {
        Integer count = this.query().eq("name", name).count();
        if (count > 0) {
            throw new ServiceException(500, "不行,不可重名");
        }
    }

    @Override
    public ContractTempleteResult add(ContractTempleteParam param) {
        ContractTemplete entity = getEntity(param);
        this.checkName(param.getName());
        this.save(entity);
        if (ToolUtil.isNotEmpty(param.getDetailParams())) {
            List<ContractTempleteDetail> contractTempleteDetails = new ArrayList<>();
            for (ContractTempleteDetailParam detailParam : param.getDetailParams()) {
                ContractTempleteDetail detail = new ContractTempleteDetail();
                ToolUtil.copyProperties(detailParam, detail);
                detail.setContractTempleteId(entity.getContractTemplateId());
                contractTempleteDetails.add(detail);
            }
            contractTempleteDetailService.saveBatch(contractTempleteDetails);
        }
        ContractTempleteResult result = new ContractTempleteResult();
        ToolUtil.copyProperties(entity, result);
        return result;
    }

    @Override
    public void delete(ContractTempleteParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(ContractTempleteParam param) {
        ContractTemplete oldEntity = getOldEntity(param);
        if (!param.getName().equals(oldEntity.getName())) {
            this.checkName(param.getName());
        }

        ContractTemplete newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        newEntity.setDisplay(null);
        this.updateById(newEntity);
        contractTempleteDetailService.remove(new QueryWrapper<ContractTempleteDetail>(){{
            eq("contract_templete_id", newEntity.getContractTemplateId());
        }});
        if (ToolUtil.isNotEmpty(param.getDetailParams())) {
            List<ContractTempleteDetail> contractTempleteDetails = new ArrayList<>();
            for (ContractTempleteDetailParam detailParam : param.getDetailParams()) {
                ContractTempleteDetail detail = new ContractTempleteDetail();
                ToolUtil.copyProperties(detailParam, detail);
                detail.setContractTempleteId(newEntity.getContractTemplateId());
                contractTempleteDetails.add(detail);
            }
            contractTempleteDetailService.saveBatch(contractTempleteDetails);
        }
    }

    @Override
    public ContractTempleteResult findBySpec(ContractTempleteParam param) {
        return null;
    }

    @Override
    public List<ContractTempleteResult> findListBySpec(ContractTempleteParam param) {
        return null;
    }

    @Override
    public PageInfo<ContractTempleteResult> findPageBySpec(ContractTempleteParam param) {
        Page<ContractTempleteResult> pageContext = getPageContext();
        IPage<ContractTempleteResult> page = this.baseMapper.customPageList(pageContext, param);
        this.format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ContractTempleteParam param) {
        return param.getContractTemplateId();
    }

    private Page<ContractTempleteResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ContractTemplete getOldEntity(ContractTempleteParam param) {
        return this.getById(getKey(param));
    }

    private ContractTemplete getEntity(ContractTempleteParam param) {
        ContractTemplete entity = new ContractTemplete();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    private void format(List<ContractTempleteResult> results) {
        List<Long> ids = new ArrayList<>();
        for (ContractTempleteResult result : results) {
            ids.add(result.getContractTemplateId());
        }
        List<ContractTempleteDetail> details = ids.size() == 0 ? new ArrayList<>() : contractTempleteDetailService.query().in("contract_templete_id", ids).list();
        List<ContractTempleteDetailResult> detailResultList = new ArrayList<>();
        for (ContractTempleteDetail detail : details) {
            ContractTempleteDetailResult result = new ContractTempleteDetailResult();
            ToolUtil.copyProperties(detail, result);
            detailResultList.add(result);
        }

        for (ContractTempleteResult result : results) {
            List<ContractTempleteDetailResult> detailResults = new ArrayList<>();
            for (ContractTempleteDetailResult contractTempleteDetailResult : detailResultList) {
                if (result.getContractTemplateId().equals(contractTempleteDetailResult.getContractTempleteId())) {
                    detailResults.add(contractTempleteDetailResult);
                }
            }
            result.setDetailResults(detailResults);
        }
    }

}
