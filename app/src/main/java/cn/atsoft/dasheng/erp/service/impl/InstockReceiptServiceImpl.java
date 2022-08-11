package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.CodingRules;
import cn.atsoft.dasheng.erp.entity.InstockLogDetail;
import cn.atsoft.dasheng.erp.entity.InstockReceipt;
import cn.atsoft.dasheng.erp.mapper.InstockReceiptMapper;
import cn.atsoft.dasheng.erp.model.params.InstockOrderParam;
import cn.atsoft.dasheng.erp.model.params.InstockReceiptParam;
import cn.atsoft.dasheng.erp.model.result.InstockReceiptResult;
import cn.atsoft.dasheng.erp.service.CodingRulesService;
import cn.atsoft.dasheng.erp.service.InstockReceiptService;
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
 * 入库记录单 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-08-11
 */
@Service
public class InstockReceiptServiceImpl extends ServiceImpl<InstockReceiptMapper, InstockReceipt> implements InstockReceiptService {

    @Autowired
    private CodingRulesService codingRulesService;

    @Override
    public void add(InstockReceiptParam param) {
        InstockReceipt entity = getEntity(param);
        this.save(entity);
    }

    /**
     * 入库单
     *
     * @param param
     * @param instockLogDetails
     */
    @Override
    public void addReceipt(InstockOrderParam param, List<InstockLogDetail> instockLogDetails) {

        InstockReceipt entity = new InstockReceipt();
        CodingRules codingRules = codingRulesService.query().eq("module", "1").eq("state", 1).one();
        if (ToolUtil.isNotEmpty(codingRules)) {
            String coding = codingRulesService.backCoding(codingRules.getCodingRulesId());
            entity.setCoding(coding);
        }
        entity.setInstockOrderId(param.getInstockOrderId());
        this.save(entity);

        for (InstockLogDetail instockLogDetail : instockLogDetails) {
            instockLogDetail.setReceiptId(entity.getReceiptId());
        }

    }


    @Override

    public void delete(InstockReceiptParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(InstockReceiptParam param) {
        InstockReceipt oldEntity = getOldEntity(param);
        InstockReceipt newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public InstockReceiptResult findBySpec(InstockReceiptParam param) {
        return null;
    }

    @Override
    public List<InstockReceiptResult> findListBySpec(InstockReceiptParam param) {
        return null;
    }

    @Override
    public PageInfo<InstockReceiptResult> findPageBySpec(InstockReceiptParam param) {
        Page<InstockReceiptResult> pageContext = getPageContext();
        IPage<InstockReceiptResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(InstockReceiptParam param) {
        return param.getReceiptId();
    }

    private Page<InstockReceiptResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private InstockReceipt getOldEntity(InstockReceiptParam param) {
        return this.getById(getKey(param));
    }

    private InstockReceipt getEntity(InstockReceiptParam param) {
        InstockReceipt entity = new InstockReceipt();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
