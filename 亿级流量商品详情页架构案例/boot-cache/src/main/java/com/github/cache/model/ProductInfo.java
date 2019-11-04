package com.github.cache.model;

/**
 * 商品信息
 *
 * @author qinxuewu
 * @create 19/11/3下午12:41
 * @since 1.0.0
 */


public class ProductInfo {

    private Long id;
    private String name;
    private Double price;

    public ProductInfo() {

    }

    public ProductInfo(Long id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

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
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ProductInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
