
package com.safia.go4lunch.model.nearbySearchResult;

import com.safia.go4lunch.model.nearbySearchResult.Northeast;
import com.safia.go4lunch.model.nearbySearchResult.Southwest;

public class Viewport {

    private Northeast northeast;
    private Southwest southwest;

    public Northeast getNortheast() {
        return northeast;
    }

    public void setNortheast(Northeast northeast) {
        this.northeast = northeast;
    }

    public Southwest getSouthwest() {
        return southwest;
    }

    public void setSouthwest(Southwest southwest) {
        this.southwest = southwest;
    }

}
