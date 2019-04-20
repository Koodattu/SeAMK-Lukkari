package com.seamk.mobile.eventbusevents;

/**
 * Created by Juha Ala-Rantala on 31.8.2017.
 */

public class SearchChosenEvent {
    int searchType;

    public SearchChosenEvent(int searchType) {
        this.searchType = searchType;
    }

    public int getSearchType() {
        return searchType;
    }

    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }
}
