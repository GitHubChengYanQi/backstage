package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Phone;
import cn.atsoft.dasheng.app.mapper.PhoneMapper;
import cn.atsoft.dasheng.app.model.params.PhoneParam;
import cn.atsoft.dasheng.app.model.result.PhoneResult;
import cn.atsoft.dasheng.app.service.PhoneService;
import cn.atsoft.dasheng.core.datascope.DataScope;
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
 * 服务实现类
 * </p>
 *
 * @author cheng
 * @since 2021-08-12
 */
@Service
public class PhoneServiceImpl extends ServiceImpl<PhoneMapper, Phone> implements PhoneService {

    @Override
    public Phone add(PhoneParam param) {
        if (ToolUtil.isNotEmpty(param.getPhoneNumber()) || ToolUtil.isNotEmpty(param.getTelephone())) {
            //防止添加重复电话号
            Integer count = this.query().eq("phone_number", param.getPhoneNumber()).and(i -> i.eq("display", 1)).count();
            if (count > 0) {
                throw new ServiceException(500, "不用重复添加手机号 或者  电话已经存在");
            }
            Phone entity = getEntity(param);
            this.save(entity);

        }
        throw new ServiceException(500, "手机号或者电话号 必须填写一个");


    }

    @Override
    public void delete(PhoneParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(PhoneParam param) {
        Phone oldEntity = getOldEntity(param);
        Phone newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public PhoneResult findBySpec(PhoneParam param) {
        return null;
    }

    @Override
    public List<PhoneResult> findListBySpec(PhoneParam param) {
        return null;
    }

    @Override
    public PageInfo<PhoneResult> findPageBySpec(PhoneParam param, DataScope dataScope) {
        Page<PhoneResult> pageContext = getPageContext();
        IPage<PhoneResult> page = this.baseMapper.customPageList(pageContext, param, dataScope);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(PhoneParam param) {
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
