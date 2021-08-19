package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.CrmCustomerLevel;
import cn.atsoft.dasheng.app.mapper.CrmCustomerLevelMapper;
import cn.atsoft.dasheng.app.model.params.CrmCustomerLevelParam;
import cn.atsoft.dasheng.app.model.result.CrmCustomerLevelResult;
import  cn.atsoft.dasheng.app.service.CrmCustomerLevelService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 客户级别表 服务实现类
 * </p>
 *
 * @author
 * @since 2021-07-30
 */
@Service
public class CrmCustomerLevelServiceImpl extends ServiceImpl<CrmCustomerLevelMapper, CrmCustomerLevel> implements CrmCustomerLevelService {

    @Override
    public void add(CrmCustomerLevelParam param){
        CrmCustomerLevel entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(CrmCustomerLevelParam param){
      CrmCustomerLevel byId = this.getById(param.getCustomerLevelId());
      if (ToolUtil.isEmpty(byId)){
        throw new ServiceException(500,"删除目标不存在");
      }
      param.setDisplay(0);
      this.update(param);
    }

    @Override
    public void update(CrmCustomerLevelParam param){
        CrmCustomerLevel oldEntity = getOldEntity(param);
        CrmCustomerLevel newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public CrmCustomerLevelResult findBySpec(CrmCustomerLevelParam param){
        return null;
    }

    @Override
    public List<CrmCustomerLevelResult> findListBySpec(CrmCustomerLevelParam param){
        return null;
    }

    @Override
    public PageInfo<CrmCustomerLevelResult> findPageBySpec(CrmCustomerLevelParam param){
        Page<CrmCustomerLevelResult> pageContext = getPageContext();
        IPage<CrmCustomerLevelResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(CrmCustomerLevelParam param){
        return param.getCustomerLevelId();
    }

    private Page<CrmCustomerLevelResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private CrmCustomerLevel getOldEntity(CrmCustomerLevelParam param) {
        return this.getById(getKey(param));
    }

    private CrmCustomerLevel getEntity(CrmCustomerLevelParam param) {
        CrmCustomerLevel entity = new CrmCustomerLevel();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
