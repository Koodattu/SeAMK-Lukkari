package com.seamk.mobile.objects;

/**
 * Created by Juha Ala-Rantala on 31.8.2017.
 */

public class SearchOption {

    private String searchTitle;
    private String searchDescription;
    private int searchType;

    public SearchOption(String searchTitle, String searchDescription, int searchType) {
        this.searchTitle = searchTitle;
        this.searchDescription = searchDescription;
        this.searchType = searchType;
    }

    public String getSearchTitle() {
        return searchTitle;
    }

    public void setSearchTitle(String searchTitle) {
        this.searchTitle = searchTitle;
    }

    public String getSearchDescription() {
        return searchDescription;
    }

    public void setSearchDescription(String searchDescription) {
        this.searchDescription = searchDescription;
    }

    public int getSearchType() {
        return searchType;
    }

    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }
}
