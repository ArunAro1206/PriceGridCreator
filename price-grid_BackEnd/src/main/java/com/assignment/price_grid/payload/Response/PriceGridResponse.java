package com.assignment.price_grid.payload.Response;

import java.util.List;

public class PriceGridResponse {
    private List<Integer> widths;
    private List<Integer> heights;
    private List<HeightPriceRow> prices;
    private int errorCode;

    public PriceGridResponse(List<Integer> widths, List<Integer> heights, List<HeightPriceRow> prices, int errorCode) {
        this.widths = widths;
        this.heights = heights;
        this.prices = prices;
        this.errorCode = errorCode;
    }

    public List<Integer> getWidths() {
        return widths;
    }

    public void setWidths(List<Integer> widths) {
        this.widths = widths;
    }

    public List<Integer> getHeights() {
        return heights;
    }

    public void setHeights(List<Integer> heights) {
        this.heights = heights;
    }

    public List<HeightPriceRow> getPrices() {
        return prices;
    }

    public void setPrices(List<HeightPriceRow> prices) {
        this.prices = prices;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public static class HeightPriceRow {
        private Integer height;
        private List<Double> values;

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public List<Double> getValues() {
            return values;
        }

        public void setValues(List<Double> values) {
            this.values = values;
        }
    }

}
