package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Adress;
import cn.atsoft.dasheng.app.mapper.AdressMapper;
import cn.atsoft.dasheng.app.model.params.AdressParam;
import cn.atsoft.dasheng.app.model.result.AdressResult;
import cn.atsoft.dasheng.app.service.AdressService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private CustomerService customerService;

    @BussinessLog
    @Override
    public Adress add(AdressParam param) {
        Customer customer = customerService.getById(param.getCustomerId());
        if (ToolUtil.isEmpty(customer)) {
            throw new ServiceException(500, "数据不存在");
        }else {
            Adress entity = getEntity(param);
            this.save(entity);
            return entity;
        }
    }


    @BussinessLog
    @Override
    public Adress delete(AdressParam param) {
        Customer adress = customerService.getById(param.getAdressId());
        if (ToolUtil.isEmpty(adress)) {
            throw new ServiceException(500, "删除前请确定客户");
        }else {
            param.setDisplay(0);
            this.update(param);
            Adress entity = getEntity(param);
            return entity;
        }
    }

    @BussinessLog
    @Override
    public Adress update(AdressParam param) {
        Adress oldEntity = getOldEntity(param);
        if (ToolUtil.isEmpty(oldEntity)) {
            throw new ServiceException(500, "数据不存在");
        }else {
            Adress newEntity = getEntity(param);
            newEntity.setCustomerId(null);
            ToolUtil.copyProperties(newEntity, oldEntity);
            this.updateById(oldEntity);
            return oldEntity;
        }
    }

    @Override
    public AdressResult findBySpec(AdressParam param) {
        return null;
    }

    @Override
    public List<AdressResult> findListBySpec(AdressParam param) {
        return null;
    }

    @Override
    public PageInfo<AdressResult> findPageBySpec(AdressParam param) {
        Page<AdressResult> pageContext = getPageContext();
        IPage<AdressResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(AdressParam param) {
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
