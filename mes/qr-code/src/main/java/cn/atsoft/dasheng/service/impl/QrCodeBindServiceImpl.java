package cn.atsoft.dasheng.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.entity.QrCodeBind;
import cn.atsoft.dasheng.mapper.QrCodeBindMapper;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.params.QrCodeBindParam;
import cn.atsoft.dasheng.model.result.QrCodeBindResult;
import cn.atsoft.dasheng.service.QrCodeBindService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * <p>
 * 二维码绑定 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-29
 */
@Service
public class QrCodeBindServiceImpl extends ServiceImpl<QrCodeBindMapper, QrCodeBind> implements QrCodeBindService {


    private Serializable getKey(QrCodeBindParam param) {
        return param.getOrCodeBindId();
    }

    private Page<QrCodeBindResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private QrCodeBind getOldEntity(QrCodeBindParam param) {
        return this.getById(getKey(param));
    }

    private QrCodeBind getEntity(QrCodeBindParam param) {
        QrCodeBind entity = new QrCodeBind();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
