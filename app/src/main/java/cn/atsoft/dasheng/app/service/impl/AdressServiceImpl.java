package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Adress;
import cn.atsoft.dasheng.app.mapper.AdressMapper;
import cn.atsoft.dasheng.app.model.params.AdressParam;
import cn.atsoft.dasheng.app.model.result.AdressResult;
import  cn.atsoft.dasheng.app.service.AdressService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 客户地址表 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-07-23
 */
@Service
public class AdressServiceImpl extends ServiceImpl<AdressMapper, Adress> implements AdressService {

    @Override
    public void add(AdressParam param){
        Adress entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(AdressParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(AdressParam param){
        Adress oldEntity = getOldEntity(param);
        Adress newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public AdressResult findBySpec(AdressParam param){
        return null;
    }

    @Override
    public List<AdressResult> findListBySpec(AdressParam param){
        return null;
    }

    @Override
    public PageInfo<AdressResult> findPageBySpec(AdressParam param){
        Page<AdressResult> pageContext = getPageContext();
        IPage<AdressResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(AdressParam param){
        return param.getAdressId();
    }

    private Page<AdressResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Adress getOldEntity(AdressParam param) {
        return this.getById(getKey(param));
    }

    private Adress getEntity(AdressParam param) {
        Adress entity = new Adress();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
