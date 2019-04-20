package com.seamk.mobile.eventbusevents;

/**
 * Created by Juha Ala-Rantala on 2.11.2017.
 */

public class ExtraChosenEvent {
        int itemType;

        public ExtraChosenEvent(int itemType) {
            this.itemType = itemType;
        }

        public int getItemType() {
            return itemType;
        }

        public void setItemType(int searchType) {
            this.itemType = itemType;
        }
}
