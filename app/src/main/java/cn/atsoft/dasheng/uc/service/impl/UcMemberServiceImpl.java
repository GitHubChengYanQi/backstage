package cn.atsoft.dasheng.uc.service.impl;


import cn.atsoft.dasheng.uc.entity.UcMember;
import cn.atsoft.dasheng.uc.mapper.UcMemberMapper;
import cn.atsoft.dasheng.uc.model.params.UcMemberParam;
import cn.atsoft.dasheng.uc.model.result.UcMemberResult;
import cn.atsoft.dasheng.uc.service.UcMemberService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
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
 * @author Sing
 * @since 2021-03-16
 */
@Service
public class UcMemberServiceImpl extends ServiceImpl<UcMemberMapper, UcMember> implements UcMemberService {

    @Override
    public void add(UcMemberParam param){
        UcMember entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(UcMemberParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(UcMemberParam param){
        UcMember oldEntity = getOldEntity(param);
        UcMember newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public UcMemberResult findBySpec(UcMemberParam param){
        return null;
    }

    @Override
    public List<UcMemberResult> findListBySpec(UcMemberParam param){
        return null;
    }

    @Override
    public PageInfo<UcMemberResult> findPageBySpec(UcMemberParam param){
        Page<UcMemberResult> pageContext = getPageContext();
        IPage<UcMemberResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }
    @Override
    public UcMemberResult findOne(UcMemberParam param){
        return this.baseMapper.customListByOne(param);
    }
    private Serializable getKey(UcMemberParam param){
        return null;
    }

    private Page<UcMemberResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private UcMember getOldEntity(UcMemberParam param) {
        return this.getById(getKey(param));
    }

    private UcMember getEntity(UcMemberParam param) {
        UcMember entity = new UcMember();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }


}
