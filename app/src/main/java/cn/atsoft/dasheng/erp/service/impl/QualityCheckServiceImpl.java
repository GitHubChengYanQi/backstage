package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.AttributeValues;
import cn.atsoft.dasheng.erp.entity.QualityCheck;
import cn.atsoft.dasheng.erp.mapper.QualityCheckMapper;
import cn.atsoft.dasheng.erp.model.params.QualityCheckParam;
import cn.atsoft.dasheng.erp.model.result.QualityCheckResult;
import cn.atsoft.dasheng.erp.service.QualityCheckService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 质检表 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-27
 */
@Service
public class QualityCheckServiceImpl extends ServiceImpl<QualityCheckMapper, QualityCheck> implements QualityCheckService {

    @Override
    public void add(QualityCheckParam param) {
        String jsonStr = JSONUtil.toJsonStr(param.getTools());
        param.setTool(jsonStr);
        QualityCheck entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(QualityCheckParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(QualityCheckParam param) {
        QualityCheck oldEntity = getOldEntity(param);
        QualityCheck newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public QualityCheckResult findBySpec(QualityCheckParam param) {
        return null;
    }

    @Override
    public List<QualityCheckResult> findListBySpec(QualityCheckParam param) {
        return null;
    }

    @Override
    public PageInfo<QualityCheckResult> findPageBySpec(QualityCheckParam param) {
        Page<QualityCheckResult> pageContext = getPageContext();
        IPage<QualityCheckResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(QualityCheckParam param) {
        return param.getQualityCheckId();
    }

    private Page<QualityCheckResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private QualityCheck getOldEntity(QualityCheckParam param) {
        return this.getById(getKey(param));
    }

    private QualityCheck getEntity(QualityCheckParam param) {
        QualityCheck entity = new QualityCheck();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    public void format(List<QualityCheckResult> data) {
        List<String> jsonids = new ArrayList<>();
        for (QualityCheckResult datum : data) {
            jsonids.add(datum.getTool());
        }
        for (String jsonid : jsonids) {
            JSONArray jsonArray = JSONUtil.parseArray(jsonid);
            
        }
    }
}
