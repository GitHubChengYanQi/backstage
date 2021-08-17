package cn.atsoft.dasheng.uc.service.impl;


import cn.atsoft.dasheng.base.oshi.util.IpInfoUtils;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.uc.entity.UcSmsCode;
import cn.atsoft.dasheng.uc.mapper.UcSmsCodeMapper;
import cn.atsoft.dasheng.uc.model.params.UcSmsCodeParam;
import cn.atsoft.dasheng.uc.model.result.UcSmsCodeResult;
import cn.atsoft.dasheng.uc.service.UcSmsCodeService;
import cn.atsoft.dasheng.core.util.HttpContext;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * -  服务实现类
 * </p>
 *
 * @author Sing
 * @since 2021-03-16
 */
@Service
public class UcSmsCodeServiceImpl extends ServiceImpl<UcSmsCodeMapper, UcSmsCode> implements UcSmsCodeService {

    public String getCode(String phone) {
        String Code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
        UcSmsCodeParam param = new UcSmsCodeParam();

        param.setCode(Code);
        HttpServletRequest request = HttpContext.getRequest();
        param.setIp(IpInfoUtils.getIpAddr(request));
        param.setDevice(request.getHeader("User-Agent"));
        param.setMobile(phone);
        this.add(param);
        return Code;
    }

    @Override
    public void add(UcSmsCodeParam param) {
        UcSmsCode entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(UcSmsCodeParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(UcSmsCodeParam param) {
        UcSmsCode oldEntity = getOldEntity(param);
        UcSmsCode newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public UcSmsCodeResult findBySpec(UcSmsCodeParam param) {
        return null;
    }

    @Override
    public List<UcSmsCodeResult> findListBySpec(UcSmsCodeParam param) {
        return null;
    }

    @Override
    public PageInfo<UcSmsCodeResult> findPageBySpec(UcSmsCodeParam param) {
        Page<UcSmsCodeResult> pageContext = getPageContext();
        IPage<UcSmsCodeResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(UcSmsCodeParam param) {
        return param.getSmsId();
    }

    private Page<UcSmsCodeResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private UcSmsCode getOldEntity(UcSmsCodeParam param) {
        return this.getById(getKey(param));
    }

    private UcSmsCode getEntity(UcSmsCodeParam param) {
        UcSmsCode entity = new UcSmsCode();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
