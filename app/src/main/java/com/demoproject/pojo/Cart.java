package com.demoproject.pojo;

public class Cart {
    private Integer id;
    private Integer itemQuantity;
    private Product product;
    private Variant variant;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Product getProduct() {
        return this.product;
    }

    public Variant getVariant() {
        return this.variant;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setVariant(Variant variant) {
        this.variant = variant;
    }

    public Integer getItemQuantity() {
        return this.itemQuantity;
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }
}
