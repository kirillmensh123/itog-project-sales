package model;

public class SaleRecord {

    private String region;
    private String country;
    private String product;
    private int unitsSold;
    private double totalRevenue;

    public SaleRecord(String region, String country, String product,
                      int unitsSold, double totalRevenue) {
        this.region = region;
        this.country = country;
        this.product = product;
        this.unitsSold = unitsSold;
        this.totalRevenue = totalRevenue;
    }

    public String getRegion() {
        return region;
    }

    public String getCountry() {
        return country;
    }

    public String getProduct() {
        return product;
    }

    public int getUnitsSold() {
        return unitsSold;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }
}
