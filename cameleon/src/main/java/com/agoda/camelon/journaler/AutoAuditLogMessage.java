package com.agoda.camelon.journaler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class AutoAuditLogMessage {
    private String correlationId;
    private String userId;
    private String mutationName;
    private String spName;
    private List<String> params;
    private String Status;

    public AutoAuditLogMessage(String correlationId, String userId, String mutationName, String spName, List<String> params, String status) {
        this.correlationId = correlationId;
        this.userId = userId;
        this.mutationName = mutationName;
        this.spName = spName;
        this.params = params;
        Status = status;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMutationName() {
        return mutationName;
    }

    public void setMutationName(String mutationName) {
        this.mutationName = mutationName;
    }

    public String getSpName() {
        return spName;
    }

    public void setSpName(String spName) {
        this.spName = spName;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String toJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }

    public String getCorrelationId() {
        return correlationId;
    }
}
