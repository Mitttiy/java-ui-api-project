package ru.ibs.gasu.client.crypto;

public class DesEntry {

    private long userId;
    private String cert;
    private String cerSNILS;
    private String status;
    private String data;
    private String signAttemptType;
    private String recordKey;
    private String message;
    private String signature;
    private Integer espepCode;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getCert() {
        return cert;
    }

    public void setCert(String cert) {
        this.cert = cert;
    }

    public String getCerSNILS() {
        return cerSNILS;
    }

    public void setCerSNILS(String cerSNILS) {
        this.cerSNILS = cerSNILS;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSignAttemptType() {
        return signAttemptType;
    }

    public void setSignAttemptType(String signAttemptType) {
        this.signAttemptType = signAttemptType;
    }

    public String getRecordKey() {
        return recordKey;
    }

    public void setRecordKey(String recordKey) {
        this.recordKey = recordKey;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Integer getEspepCode() {
        return espepCode;
    }

    public void setEspepCode(Integer espepCode) {
        this.espepCode = espepCode;
    }
}
