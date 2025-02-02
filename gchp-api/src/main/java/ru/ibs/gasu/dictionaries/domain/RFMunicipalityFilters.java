package ru.ibs.gasu.dictionaries.domain;

public class RFMunicipalityFilters {
    private String regionId;
    private String oktmo;
    private Long oktmoType;

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getOktmo() {
        return oktmo;
    }

    public void setOktmo(String oktmo) {
        this.oktmo = oktmo;
    }

    public Long getOktmoType() {
        return oktmoType;
    }

    public void setOktmoType(Long oktmoType) {
        this.oktmoType = oktmoType;
    }
}
