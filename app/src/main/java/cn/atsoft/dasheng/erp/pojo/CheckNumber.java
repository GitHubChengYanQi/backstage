package cn.atsoft.dasheng.erp.pojo;

import lombok.Data;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.util.List;

/**
 * 异常复核数
 */
@Data
public class CheckNumber {
    private Integer number;
    private String name;
    private Long userId;
    private String note;
    private List<Long> mediaIds;
    private List<String> mediaUrls;
}
