package ru.ibs.gasu.common.models;

public class CourtActivityModel {
    private Long id;
    private Long gid;
    private Long date;
    private String disputeSubject;
    private String judgment;
    private String kadArbitrRuUrl;
    private String comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGid() {
        return gid;
    }

    public void setGid(Long gid) {
        this.gid = gid;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getDisputeSubject() {
        return disputeSubject;
    }

    public void setDisputeSubject(String disputeSubject) {
        this.disputeSubject = disputeSubject;
    }

    public String getJudgment() {
        return judgment;
    }

    public void setJudgment(String judgment) {
        this.judgment = judgment;
    }

    public String getKadArbitrRuUrl() {
        return kadArbitrRuUrl;
    }

    public void setKadArbitrRuUrl(String kadArbitrRuUrl) {
        this.kadArbitrRuUrl = kadArbitrRuUrl;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
