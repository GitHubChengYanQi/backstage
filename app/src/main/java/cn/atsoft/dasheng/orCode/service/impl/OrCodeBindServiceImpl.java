package cn.atsoft.dasheng.orCode.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.orCode.BindParam;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.mapper.OrCodeBindMapper;
import cn.atsoft.dasheng.orCode.model.params.OrCodeBindParam;
import cn.atsoft.dasheng.orCode.model.result.OrCodeBindResult;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 二维码绑定 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-29
 */
@Service
public class OrCodeBindServiceImpl extends ServiceImpl<OrCodeBindMapper, OrCodeBind> implements OrCodeBindService {
    @Transactional
    @Override
    public OrCodeBind add(OrCodeBindParam param) {
        OrCodeBind entity = getEntity(param);
        this.save(entity);
        return entity;
    }

    @Override
    public void delete(OrCodeBindParam param) {
        throw new ServiceException(500, "不可以删除");
    }

    @Override
    public void update(OrCodeBindParam param) {

        throw new ServiceException(500, "不可以修改");
    }

    @Override
    public OrCodeBindResult findBySpec(OrCodeBindParam param) {
        return null;
    }

    @Override
    public List<OrCodeBindResult> findListBySpec(OrCodeBindParam param) {
        return null;
    }

    @Override
    public PageInfo<OrCodeBindResult> findPageBySpec(OrCodeBindParam param) {
        Page<OrCodeBindResult> pageContext = getPageContext();
        IPage<OrCodeBindResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public List<Long> backValue(List<Long> qrcodeIds) {
        if (ToolUtil.isEmpty(qrcodeIds)) {
            new ArrayList<>();
        }
        List<OrCodeBind> binds = this.query().in("qr_code_id", qrcodeIds).list();
        List<Long> formIds = new ArrayList<>();
        for (OrCodeBind bind : binds) {
            formIds.add(bind.getFormId());
        }
        return formIds;
    }

    /**
     * 返回绑定二维码
     *
     * @param formId
     * @return
     */
    @Override
    public Long getQrcodeId(Long formId) {
        if (ToolUtil.isEmpty(formId)) {
            return null;
        }
        OrCodeBind bind = this.query().eq("form_id", formId).one();
        if (ToolUtil.isEmpty(bind)) {
            throw new ServiceException(500, "此物未绑定二维码");
        }
        return bind.getOrCodeId();

    }

    @Override
    public Long getFormId(Long qrcodeId) {

        if (ToolUtil.isEmpty(qrcodeId)) {
            throw new ServiceException(500, "请传入二维码");
        }
        OrCodeBind codeBind = this.query().eq("qr_code_id", qrcodeId).one();
        if (ToolUtil.isEmpty(codeBind)) {
            throw new ServiceException(500, "此码未绑定");
        }
        Long formId = codeBind.getFormId();
        return formId;
    }

    @Override
    public List<Long> getFormIds(List<Long> qrcodeIds) {
        if (ToolUtil.isEmpty(qrcodeIds)) {
            return new ArrayList<>();
        }
        List<OrCodeBind> codeBinds = this.query().in("qr_code_id", qrcodeIds).list();
        List<Long> formIds = new ArrayList<>();
        for (OrCodeBind codeBind : codeBinds) {
            formIds.add(codeBind.getFormId());
        }
        return formIds;
    }

    @Override
    public List<Long> getCodeIds(List<Long> fromIds) {
        if (ToolUtil.isEmpty(fromIds)) {
            return new ArrayList<>();
        }
        List<OrCodeBind> orCodeBinds = this.query().in("form_id", fromIds).list();
        if (ToolUtil.isEmpty(orCodeBinds)) {
            return new ArrayList<>();
        }

        List<Long> qrcodeIds = new ArrayList<>();
        for (OrCodeBind orCodeBind : orCodeBinds) {
            qrcodeIds.add(orCodeBind.getOrCodeId());
        }
        return qrcodeIds;
    }

    private Serializable getKey(OrCodeBindParam param) {
        return param.getOrCodeBindId();
    }

    private Page<OrCodeBindResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private OrCodeBind getOldEntity(OrCodeBindParam param) {
        return this.getById(getKey(param));
    }

    private OrCodeBind getEntity(OrCodeBindParam param) {
        OrCodeBind entity = new OrCodeBind();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
