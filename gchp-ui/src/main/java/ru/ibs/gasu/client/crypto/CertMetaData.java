package ru.ibs.gasu.client.crypto;

public class CertMetaData {

    private String subjectName;
    private String type;
    private String validFrom;
    private String validTo;
    private String publicKey;
    private String serialNum;
    private String signAlg;
    private String issuer;
    private String base64;
    private String snils;

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getValidTo() {
        return validTo;
    }

    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getSignAlg() {
        return signAlg;
    }

    public void setSignAlg(String signAlg) {
        this.signAlg = signAlg;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public String getSnils() {
        return snils;
    }

    public void setSnils(String snils) {
        this.snils = snils;
    }

    @Override
    public String toString() {
        return "CertMetaData{" +
                "subjectName='" + subjectName + '\'' +
                ", type='" + type + '\'' +
                ", validFrom='" + validFrom + '\'' +
                ", validTo='" + validTo + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", serialNum='" + serialNum + '\'' +
                ", signAlg='" + signAlg + '\'' +
                ", issuer='" + issuer + '\'' +
                ", base64='" + base64 + '\'' +
                ", snils='" + snils + '\'' +
                '}';
    }
}
