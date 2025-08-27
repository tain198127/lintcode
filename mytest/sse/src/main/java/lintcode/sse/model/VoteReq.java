package lintcode.sse.model;

import lombok.Data;

@Data
public class VoteReq {
    private String userId;
    private String topic;
    private String vote;
}
