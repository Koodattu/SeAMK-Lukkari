package com.seamk.mobile.objects;

/**
 * Created by Juha Ala-Rantala on 5.4.2017.
 */

public class Course {
    private String titlefi;
    private String titleen;
    private String category;
    private String prices;
    private String properties;

    public Course(String titlefi, String titleen, String category, String prices, String properties) {
        if (titlefi.contains("&amp;")){
            titlefi = titlefi.replace("&amp;", " & ");
        }
        if (titleen.contains("&amp;")){
            titleen = titleen.replace("&amp;", " & ");
        }
        this.titlefi = titlefi;
        this.titleen = titleen;
        this.category = category;

        if (prices != null){
            String x = prices;
            String[] splitted = x.split(" / ");

            String newPrices = splitted[0] + "€";

            for (int i = 1; i < splitted.length; i++){
                newPrices += " / " + splitted[i] + "€";
            }

            this.prices = newPrices;
        }

        this.properties = properties;
    }

    public String getTitlefi() {
        return titlefi;
    }

    public void setTitlefi(String titlefi) {
        this.titlefi = titlefi;
    }

    public String getTitleen() {
        return titleen;
    }

    public void setTitleen(String titleen) {
        this.titleen = titleen;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrices() {
        return prices;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }
}
