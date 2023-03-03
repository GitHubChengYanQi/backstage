package cn.atsoft.dasheng.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.entity.QrCode;

import cn.atsoft.dasheng.mapper.QrCodeMapper;
import cn.atsoft.dasheng.model.params.QrCodeParam;


import cn.atsoft.dasheng.model.result.QrCodeResult;


import cn.atsoft.dasheng.service.QrCodeService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * <p>
 * 二维码 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-29
 */
@Service
public class QrCodeServiceImpl extends ServiceImpl<QrCodeMapper, QrCode> implements QrCodeService {


    private Serializable getKey(QrCodeParam param) {
        return param.getOrCodeId();
    }

    private Page<QrCodeResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private QrCode getOldEntity(QrCodeParam param) {
        return this.getById(getKey(param));
    }

    private QrCode getEntity(QrCodeParam param) {
        QrCode entity = new QrCode();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }



}






