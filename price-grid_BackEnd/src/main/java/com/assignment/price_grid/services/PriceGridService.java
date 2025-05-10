package com.assignment.price_grid.services;

import com.assignment.price_grid.model.PriceCell;
import com.assignment.price_grid.payload.Request.CreatePriceGridRequest;
import com.assignment.price_grid.payload.Response.CreatePriceGridResponse;
import com.assignment.price_grid.payload.Response.PriceGridResponse;
import com.assignment.price_grid.payload.Response.UpdatePriceResponse;
import com.assignment.price_grid.repository.PriceGridRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PriceGridService {

    @Autowired
    private PriceGridRepository priceGridRepository;


    public CreatePriceGridResponse createPriceGrid(CreatePriceGridRequest createPriceGridRequest){
        int width= createPriceGridRequest.getWidth();
        int height= createPriceGridRequest.getHeight();
        double price= createPriceGridRequest.getPrice();

        PriceCell priceCell=new PriceCell(width,height,price);
        priceGridRepository.save(priceCell);
        return new CreatePriceGridResponse(true,0);
    }
    public PriceGridResponse getPriceGrid() {
        List<PriceCell> priceCellList = priceGridRepository.findAll();

        if (priceCellList.isEmpty()) {
            return new PriceGridResponse(List.of(), List.of(), List.of(), 1);
        }

        Set<Integer> uniqueHeights = priceCellList.stream().map(PriceCell::getHeight).collect(Collectors.toSet());
        Set<Integer> uniqueWidths = priceCellList.stream().map(PriceCell::getWidth).collect(Collectors.toSet());

        List<Integer> sortedHeights = uniqueHeights.stream().sorted().toList();
        List<Integer> sortedWidths = uniqueWidths.stream().sorted().toList();

        List<PriceGridResponse.HeightPriceRow> rows = new ArrayList<>();
        for (Integer height : sortedHeights) {
            List<Double> rowPrices = sortedWidths.stream()
                    .map(width -> priceCellList.stream()
                            .filter(e -> e.getHeight() == height && e.getWidth() == width)
                            .map(PriceCell::getPrice)
                            .findFirst()
                            .orElse(0.0))
                    .toList();

            PriceGridResponse.HeightPriceRow row = new PriceGridResponse.HeightPriceRow();
            row.setHeight(height);
            row.setValues(rowPrices);
            rows.add(row);
        }
        return new PriceGridResponse(sortedHeights, sortedWidths, rows, 0);
    }

    public UpdatePriceResponse updatePrices(List<PriceCell> updatedEntries) {
        if (updatedEntries == null || updatedEntries.isEmpty()) {
            return new UpdatePriceResponse(false, "No data to update.");
        }

        try {
            priceGridRepository.saveAll(updatedEntries);
            return new UpdatePriceResponse(true, "Prices updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return new UpdatePriceResponse(false, "Failed to update prices: " + e.getMessage());
        }
    }
}
