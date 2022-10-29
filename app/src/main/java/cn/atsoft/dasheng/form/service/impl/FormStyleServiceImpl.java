package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.FormStyle;
import cn.atsoft.dasheng.form.mapper.FormStyleMapper;
import cn.atsoft.dasheng.form.model.params.FormStyleParam;
import cn.atsoft.dasheng.form.model.result.FormStyleResult;
import cn.atsoft.dasheng.form.service.FormStyleService;
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
 * 表单风格 服务实现类
 * </p>
 *
 * @author
 * @since 2022-09-23
 */
@Service
public class FormStyleServiceImpl extends ServiceImpl<FormStyleMapper, FormStyle> implements FormStyleService {

    @Override
    public void add(FormStyleParam param) {
        FormStyle entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(FormStyleParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(FormStyleParam param) {

        FormStyle detail = this.lambdaQuery().eq(FormStyle::getFormType, param.getFormType()).eq(FormStyle::getDisplay, 1).orderByDesc(FormStyle::getCreateTime).last(" limit 1").one();

        if (ToolUtil.isEmpty(detail)) {
            throw new ServiceException(500, "没找到该类型下的表单数据，修改失败!");
        }
        FormStyleParam formStyleParam = new FormStyleParam();
        ToolUtil.copyProperties(detail, formStyleParam);
        FormStyle newEntity = getEntity(param);
        newEntity.setStyleId(detail.getStyleId());
        this.updateById(newEntity);
    }

    @Override
    public FormStyleResult findBySpec(FormStyleParam param) {
        return null;
    }

    @Override
    public List<FormStyleResult> findListBySpec(FormStyleParam param) {
        return null;
    }

    @Override
    public PageInfo<FormStyleResult> findPageBySpec(FormStyleParam param) {
        Page<FormStyleResult> pageContext = getPageContext();
        IPage<FormStyleResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(FormStyleParam param) {
        return param.getStyleId();
    }

    private Page<FormStyleResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private FormStyle getOldEntity(FormStyleParam param) {
        return this.getById(getKey(param));
    }

    private FormStyle getEntity(FormStyleParam param) {
        FormStyle entity = new FormStyle();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
