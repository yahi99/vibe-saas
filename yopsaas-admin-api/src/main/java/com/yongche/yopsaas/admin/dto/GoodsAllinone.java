package com.yongche.yopsaas.admin.dto;

import com.yongche.yopsaas.db.domain.YopsaasGoods;
import com.yongche.yopsaas.db.domain.YopsaasGoodsAttribute;
import com.yongche.yopsaas.db.domain.YopsaasGoodsProduct;
import com.yongche.yopsaas.db.domain.YopsaasGoodsSpecification;

public class GoodsAllinone {
    YopsaasGoods goods;
    YopsaasGoodsSpecification[] specifications;
    YopsaasGoodsAttribute[] attributes;
    YopsaasGoodsProduct[] products;

    public YopsaasGoods getGoods() {
        return goods;
    }

    public void setGoods(YopsaasGoods goods) {
        this.goods = goods;
    }

    public YopsaasGoodsProduct[] getProducts() {
        return products;
    }

    public void setProducts(YopsaasGoodsProduct[] products) {
        this.products = products;
    }

    public YopsaasGoodsSpecification[] getSpecifications() {
        return specifications;
    }

    public void setSpecifications(YopsaasGoodsSpecification[] specifications) {
        this.specifications = specifications;
    }

    public YopsaasGoodsAttribute[] getAttributes() {
        return attributes;
    }

    public void setAttributes(YopsaasGoodsAttribute[] attributes) {
        this.attributes = attributes;
    }

}
