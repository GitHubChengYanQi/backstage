package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Phone;
import cn.atsoft.dasheng.app.mapper.PhoneMapper;
import cn.atsoft.dasheng.app.model.params.PhoneParam;
import cn.atsoft.dasheng.app.model.result.PhoneResult;
import  cn.atsoft.dasheng.app.service.PhoneService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
 * @author cheng
 * @since 2021-08-12
 */
@Service
public class PhoneServiceImpl extends ServiceImpl<PhoneMapper, Phone> implements PhoneService {

    @Override
    public void add(PhoneParam param){
        //查询电话号码是否已存在
        QueryWrapper<Phone> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Phone::getPhoneNumber,param.getPhoneNumber()).eq(Phone::getContactsId,param.getContactsId());
        List<Phone> list = baseMapper.selectList(queryWrapper);
        if(ToolUtil.isEmpty(list)){
            Phone entity = getEntity(param);
            this.save(entity);
        }else {
            throw new ServiceException(500,"此电话号码已存在");
        }

    }

    @Override
    public void delete(PhoneParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(PhoneParam param){
        Phone oldEntity = getOldEntity(param);
        Phone newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public PhoneResult findBySpec(PhoneParam param){
        return null;
    }

    @Override
    public List<PhoneResult> findListBySpec(PhoneParam param){
        return null;
    }

    @Override
    public PageInfo<PhoneResult> findPageBySpec(PhoneParam param){
        Page<PhoneResult> pageContext = getPageContext();
        IPage<PhoneResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(PhoneParam param){
        return param.getPhoneId();
    }

    private Page<PhoneResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Phone getOldEntity(PhoneParam param) {
        return this.getById(getKey(param));
    }

    private Phone getEntity(PhoneParam param) {
        Phone entity = new Phone();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
