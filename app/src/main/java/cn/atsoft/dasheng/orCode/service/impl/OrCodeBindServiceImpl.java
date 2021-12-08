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
    public void add(OrCodeBindParam param) {
        OrCodeBind entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(OrCodeBindParam param) {
//        this.removeById(getKey(param));
        throw new ServiceException(500, "不可以删除");
    }

    @Override
    public void update(OrCodeBindParam param) {
//        OrCodeBind oldEntity = getOldEntity(param);
//        OrCodeBind newEntity = getEntity(param);
//        ToolUtil.copyProperties(newEntity, oldEntity);
//        this.updateById(newEntity);
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
            return null;
        }
        return bind.getOrCodeId();

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
