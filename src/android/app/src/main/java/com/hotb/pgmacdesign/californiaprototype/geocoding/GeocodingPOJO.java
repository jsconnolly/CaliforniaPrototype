package com.hotb.pgmacdesign.californiaprototype.geocoding;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pmacdowell on 2017-02-22.
 */

public class GeocodingPOJO {

    @SerializedName("status")
    private String status;
    @SerializedName("results")
    private Results[] results;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Results[] getResults() {
        return results;
    }

    public void setResults(Results[] results) {
        this.results = results;
    }

    public static class Results {
        @SerializedName("place_id")
        private String place_id;
        @SerializedName("formatted_address")
        private String formatted_address;
        @SerializedName("geometry")
        private Geometry geometry;
        @SerializedName("types")
        private String[] types;

        public String getPlace_id() {
            return place_id;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }

        public String getFormatted_address() {
            return formatted_address;
        }

        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }

        public Geometry getGeometry() {
            return geometry;
        }

        public void setGeometry(Geometry geometry) {
            this.geometry = geometry;
        }

        public String[] getTypes() {
            return types;
        }

        public void setTypes(String[] types) {
            this.types = types;
        }
    }


    public static class Geometry {
        @SerializedName("location")
        private GeocodingLocation location;

        public GeocodingLocation getLocation() {
            return location;
        }

        public void setLocation(GeocodingLocation location) {
            this.location = location;
        }
    }

    public static class GeocodingLocation {
        @SerializedName("lat")
        private double lat;
        @SerializedName("lng")
        private double lng;

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }
    }
}
