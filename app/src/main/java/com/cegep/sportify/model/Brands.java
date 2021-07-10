package com.cegep.sportify.model;

public class Brands {

    private Product Products;

    private String adminId;

    private String brandname;

    private String email;

    private String image;

    public Brands() {
    }

    public Brands(Product product, String admin, String brandname, String email, String image) {
        this.Products = product;
        this.adminId = admin;
        this.brandname = brandname;
        this.email = email;
        this.image = image;
    }

    public Product getProduct() {
        return Products;
    }

    public void setProduct(Product product) {
        this.Products = product;
    }

    public String getAdminID() {
        return adminId;
    }

    public void setAdminID(String adminID) {
        this.adminId = adminID;
    }

    public String getBrand() {
        return brandname;
    }

    public void setBrand(String brandname) {
        this.brandname = brandname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageURl() {
        return image;
    }

    public void setImageURl(String imageURl) {
        this.image = imageURl;
    }
}
