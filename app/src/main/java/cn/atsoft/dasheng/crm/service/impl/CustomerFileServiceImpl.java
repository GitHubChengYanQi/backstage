package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.CustomerFile;
import cn.atsoft.dasheng.crm.mapper.CustomerFileMapper;
import cn.atsoft.dasheng.crm.model.params.CustomerFileParam;
import cn.atsoft.dasheng.crm.model.result.CustomerFileResult;
import  cn.atsoft.dasheng.crm.service.CustomerFileService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2021-09-08
 */
@Service
public class CustomerFileServiceImpl extends ServiceImpl<CustomerFileMapper, CustomerFile> implements CustomerFileService {

    @Override
    public void add(CustomerFileParam param){

        CustomerFile entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(CustomerFileParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(CustomerFileParam param){
        CustomerFile oldEntity = getOldEntity(param);
        CustomerFile newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public CustomerFileResult findBySpec(CustomerFileParam param){
        return null;
    }

    @Override
    public List<CustomerFileResult> findListBySpec(CustomerFileParam param){
        return null;
    }

    @Override
    public PageInfo<CustomerFileResult> findPageBySpec(CustomerFileParam param){
        Page<CustomerFileResult> pageContext = getPageContext();
        IPage<CustomerFileResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(CustomerFileParam param){
        return param.getFileId();
    }

    private Page<CustomerFileResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private CustomerFile getOldEntity(CustomerFileParam param) {
        return this.getById(getKey(param));
    }

    private CustomerFile getEntity(CustomerFileParam param) {
        CustomerFile entity = new CustomerFile();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
