package com.example.entity;

import java.math.BigDecimal;

public class Account {

    private Long id;

    /**用户id*/
    private Long userId;



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
                ", residue=" + residue +
                '}';
    }
}
