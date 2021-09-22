package cn.atsoft.dasheng.crm.model.params;

import lombok.Data;

import java.util.List;

@Data
public class CompetitorIdsRequest {
    private List<Long> competitorId;
}
