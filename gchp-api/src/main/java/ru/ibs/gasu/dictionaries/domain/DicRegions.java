package ru.ibs.gasu.dictionaries.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "DIC_REGIONS")
public class DicRegions implements Serializable

    {

        private static final long serialVersionUID = 1933091132397827929L;

        @Id
        private String id;

        @Column(length = 2000)
        private String name;

        @Column(length = 100, name = "REGION_ICON")
        private String regionIcon;

        public String getId() {
        return id;
    }

        public void setId(String id) {
        this.id = id;
    }

        public String getName() {
        return name;
    }

        public void setName(String name) {
        this.name = name;
    }

        public String getRegionIcon() {
        return regionIcon;
    }

        public void setRegionIcon(String regionIcon) {
        this.regionIcon = regionIcon;
    }
    }
