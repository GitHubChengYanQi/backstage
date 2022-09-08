package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.app.entity.Contacts;
import cn.atsoft.dasheng.app.entity.Phone;
import cn.atsoft.dasheng.app.model.result.ContactsResult;
import cn.atsoft.dasheng.app.service.PhoneService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.ContactsBind;
import cn.atsoft.dasheng.crm.mapper.ContactsBindMapper;
import cn.atsoft.dasheng.crm.model.params.ContactsBindParam;
import cn.atsoft.dasheng.crm.model.result.ContactsBindResult;
import cn.atsoft.dasheng.crm.service.ContactsBindService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 联系人绑定表 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-09-22
 */
@Service
public class ContactsBindServiceImpl extends ServiceImpl<ContactsBindMapper, ContactsBind> implements ContactsBindService {
    @Autowired
    private PhoneService phoneService;

    @Override
    public void add(ContactsBindParam param) {
        ContactsBind entity = getEntity(param);
        this.save(entity);
    }

    @Override
    @Transactional
    public void delete(ContactsBindParam param) {

        if (ToolUtil.isEmpty(param.getCustomerId())) {   //删除当前联系人 所有绑定
            QueryWrapper<ContactsBind> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("contacts_id", param.getContactsId());
            ContactsBind contactsBind = new ContactsBind();
            contactsBind.setDisplay(0);
            this.update(contactsBind, queryWrapper);
        }

        if (ToolUtil.isNotEmpty(param.getCustomerIds())) {    //当前客户与当前公司离职
            QueryWrapper<ContactsBind> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("contacts_id", param.getContactsId());
            queryWrapper.in("customer_id", param.getCustomerIds());
            ContactsBind contactsBind = new ContactsBind();
            contactsBind.setDisplay(0);
            this.update(contactsBind, queryWrapper);
        }


        if (ToolUtil.isNotEmpty(param.getNewCustomerId())) {   //复职
            ContactsBind bind = null;
            bind = this.query().eq("customer_id", param.getNewCustomerId()).eq("contacts_id", param.getContactsId()).one();
            if (ToolUtil.isNotEmpty(bind)) {
                bind.setDisplay(1);
                this.updateById(bind);
            } else {
                bind = new ContactsBind();
                bind.setContactsId(param.getContactsId());
                bind.setCustomerId(param.getNewCustomerId());
                this.save(bind);
            }
        }
    }

    @Override
    public void update(ContactsBindParam param) {
        ContactsBind oldEntity = getOldEntity(param);
        ContactsBind newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ContactsBindResult findBySpec(ContactsBindParam param) {
        return null;
    }

    @Override
    public List<ContactsBindResult> findListBySpec(ContactsBindParam param) {
        return null;
    }

    @Override
    public PageInfo<ContactsBindResult> findPageBySpec(ContactsBindParam param) {
        Page<ContactsBindResult> pageContext = getPageContext();
        IPage<ContactsBindResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ContactsBindParam param) {
        return param.getContactsBindId();
    }

    private Page<ContactsBindResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ContactsBind getOldEntity(ContactsBindParam param) {
        return this.getById(getKey(param));
    }

    private ContactsBind getEntity(ContactsBindParam param) {
        ContactsBind entity = new ContactsBind();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    /**
     * 当前联系人 所在的公司 离职状态
     *
     * @param results
     * @param customerId
     */
    @Override
    public void ContractsFormat(List<ContactsResult> results, Long customerId) {
        List<ContactsBind> contactsBinds = this.query().eq("customer_id", customerId).eq("display",1).list();

        for (ContactsBind contactsBind : contactsBinds) {
            for (ContactsResult result : results) {
                if (result.getContactsId().equals(contactsBind.getContactsId())) {
                    result.setIsNotDeparture(contactsBind.getDisplay());
                    break;
                }
            }
        }
    }
}
