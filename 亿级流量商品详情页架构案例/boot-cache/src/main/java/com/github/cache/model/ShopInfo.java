package com.github.cache.model;


/**
 * 功能描述: 店铺信息
 * @author: qinxuewu
 * @date: 2019/11/4 11:23
 * @since 1.0.0
 */
public class ShopInfo {

    private Long id;
    private String name;
    private Integer level;
    private Double goodCommentRate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Double getGoodCommentRate() {
        return goodCommentRate;
    }

    public void setGoodCommentRate(Double goodCommentRate) {
        this.goodCommentRate = goodCommentRate;
    }

    @Override
    public String toString() {
        return "ShopInfo [id=" + id + ", name=" + name + ", level=" + level
                + ", goodCommentRate=" + goodCommentRate + "]";
    }
}
