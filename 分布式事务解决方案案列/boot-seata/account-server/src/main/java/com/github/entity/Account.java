package com.github.entity;

import java.math.BigDecimal;

public class Account {

    private Long id;

    /**用户id*/
    private Long userId;

    /**总额度*/
    private BigDecimal total;

    /**已用额度*/
    private BigDecimal used;

    /**剩余额度*/
    private BigDecimal residue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getUsed() {
        return used;
    }

    public void setUsed(BigDecimal used) {
        this.used = used;
    }

    public BigDecimal getResidue() {
        return residue;
    }

    public void setResidue(BigDecimal residue) {
        this.residue = residue;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", userId=" + userId +
                ", total=" + total +
                ", used=" + used +
                ", residue=" + residue +
                '}';
    }
}
