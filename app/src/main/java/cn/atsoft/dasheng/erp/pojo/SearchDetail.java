package cn.atsoft.dasheng.erp.pojo;

import lombok.Data;

@Data
public class SearchDetail {

    public SearchDetail() {
    }

    public SearchDetail(Long key, String title) {
        this.key = key;
        this.title = title;
    }

    private Long key;
    private String title;
}
