package cn.atsoft.dasheng.drafts.service;

import cn.atsoft.dasheng.base.dict.AbstractDictMap;
import cn.atsoft.dasheng.base.dict.SystemDict;
import cn.atsoft.dasheng.drafts.model.params.DraftsParam;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.METHOD)   //定义注解的使用范围为方法
@Retention(RetentionPolicy.RUNTIME )
public @interface DraftsAOP {

    long draftsId () default 0L;

    Class<?> dict() default DraftsParam.class;




}
