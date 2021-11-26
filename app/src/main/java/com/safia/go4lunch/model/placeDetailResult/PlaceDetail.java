
package com.safia.go4lunch.model.placeDetailResult;

import com.safia.go4lunch.model.nearbySearchResult.Result;

import java.util.List;

public class PlaceDetail {

    private List<Object> htmlAttributions = null;
    private PlaceDetailResult result;
    private String status;

    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public PlaceDetailResult getResult() {
        return result;
    }

    public void setResult(PlaceDetailResult result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
