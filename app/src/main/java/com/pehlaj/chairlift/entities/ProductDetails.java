package com.pehlaj.chairlift.entities;

/**
 * @author Pehlaj
 * @since 6/4/2017.
 */

public class ProductDetails extends BaseEntity {

    protected String warrantFromTime;
    protected String warrantToTime;
    protected String manufacturingDate;
    protected String firstPingDate;
    protected String registrationDate;
    protected String upc;
    protected String sku;
    protected String warrantyRegistrationDate;
    protected String imei;
    protected String imei2;
    protected String batchNo;
    protected String productModel;

    public ProductDetails() {
    }

    public String getWarrantFromTime() {
        return warrantFromTime;
    }

    public String getWarrantToTime() {
        return warrantToTime;
    }

    public String getManufacturingDate() {
        return manufacturingDate;
    }

    public String getFirstPingDate() {
        return firstPingDate;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public String getUpc() {
        return upc;
    }

    public String getSku() {
        return sku;
    }

    public String getWarrantyRegistrationDate() {
        return warrantyRegistrationDate;
    }

    public String getImei() {
        return imei;
    }

    public String getImei2() {
        return imei2;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public String getProductModel() {
        return productModel;
    }
}
