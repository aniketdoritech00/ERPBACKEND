package com.doritech.CustomerService.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "installation_hdd_config")
public class HDDConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer size1TB;
    private Integer size2TB;
    private Integer size4TB;
    private Integer size6TB;
    private Integer size8TB;
    private Integer size10TB;

    private Integer totalSize;

    @OneToOne
    @JoinColumn(name = "installation_id")
    private Installation installation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSize1TB() {
        return size1TB;
    }

    public void setSize1TB(Integer size1tb) {
        size1TB = size1tb;
    }

    public Integer getSize2TB() {
        return size2TB;
    }

    public void setSize2TB(Integer size2tb) {
        size2TB = size2tb;
    }

    public Integer getSize4TB() {
        return size4TB;
    }

    public void setSize4TB(Integer size4tb) {
        size4TB = size4tb;
    }

    public Integer getSize6TB() {
        return size6TB;
    }

    public void setSize6TB(Integer size6tb) {
        size6TB = size6tb;
    }

    public Integer getSize8TB() {
        return size8TB;
    }

    public void setSize8TB(Integer size8tb) {
        size8TB = size8tb;
    }

    public Integer getSize10TB() {
        return size10TB;
    }

    public void setSize10TB(Integer size10tb) {
        size10TB = size10tb;
    }

    public Integer getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Integer totalSize) {
        this.totalSize = totalSize;
    }

    public Installation getInstallation() {
        return installation;
    }

    public void setInstallation(Installation installation) {
        this.installation = installation;
    }

    
}
