package cn.atsoft.dasheng.action;


import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.core.util.SpringContextHolder;
import cn.atsoft.dasheng.crm.entity.DocumentsStatus;
import cn.atsoft.dasheng.crm.service.DocumentsStatusService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;

public interface FormActionInterface {

    DocumentsStatusService documentsStatusService = SpringContextHolder.getBean(DocumentsStatusService.class);

    /**
     * 通过动作获取状态码
     *
     * @param
     * @return
     */
    default Integer getStatus(String formType) {
        String name = this.toString();
        DocumentsStatus status = documentsStatusService.query().eq("form_type", formType).eq("action", name).one();
        return status.getFormStatus();
    }


    default void setStatus(int status, String formType) {
        String name = this.toString();
        DocumentsStatus documentsStatus = new DocumentsStatus();
        documentsStatus.setFormStatus(status);
        documentsStatus.setAction(name);
        documentsStatus.setFormType(formType);
        documentsStatusService.save(documentsStatus);

    }
    Long getStatus();
}
