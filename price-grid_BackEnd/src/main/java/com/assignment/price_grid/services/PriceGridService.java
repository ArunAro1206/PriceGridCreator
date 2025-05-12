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

    public CreatePriceGridResponse createPriceGrid(CreatePriceGridRequest request) {
        int width = request.getWidth();
        int height = request.getHeight();
        double price = request.getPrice();

        Optional<PriceCell> priceCellOptional = priceGridRepository.findByWidthAndHeightAndPrice(width, height, price);
        if (priceCellOptional.isEmpty()) {
            PriceCell priceCell = new PriceCell(width, height, price);
            priceGridRepository.save(priceCell);
        } else {
            return new CreatePriceGridResponse(false, 1);//1- means already created
        }
        return new CreatePriceGridResponse(true, 0);
    }


    public PriceGridResponse getPriceGrid() {
        List<PriceCell> priceCellList = priceGridRepository.findAll();

        if (priceCellList.isEmpty()) {
            return new PriceGridResponse(List.of(), List.of(), List.of(), 1);
        }

        Set<Integer> uniqueHeights = priceCellList.stream()
                .map(PriceCell::getHeight)
                .collect(Collectors.toCollection(TreeSet::new));

        Set<Integer> uniqueWidths = priceCellList.stream()
                .map(PriceCell::getWidth)
                .collect(Collectors.toCollection(TreeSet::new));

        List<Integer> sortedHeights = new ArrayList<>(uniqueHeights);
        List<Integer> sortedWidths = new ArrayList<>(uniqueWidths);

        Map<String, Double> priceMap = new HashMap<>();
        for (PriceCell cell : priceCellList) {
            String key = cell.getHeight() + "_" + cell.getWidth();
            priceMap.put(key, cell.getPrice());
        }

        List<PriceGridResponse.HeightPriceRow> rows = new ArrayList<>();
        for (Integer height : sortedHeights) {
            List<Double> rowPrices = new ArrayList<>();

            for (Integer width : sortedWidths) {
                String key = height + "_" + width;
                rowPrices.add(priceMap.getOrDefault(key, 0.0));
            }

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
            List<PriceCell> toSave = new ArrayList<>();

            for (PriceCell entry : updatedEntries) {
                Optional<PriceCell> existing = priceGridRepository.findByWidthAndHeight(entry.getWidth(), entry.getHeight());

                if (existing.isPresent()) {
                    PriceCell cell = existing.get();
                    cell.setPrice(entry.getPrice());
                    toSave.add(cell);
                } else {
                    PriceCell newCell = new PriceCell(entry.getWidth(), entry.getHeight(), entry.getPrice());
                    toSave.add(newCell);
                }
            }

            priceGridRepository.saveAll(toSave);
            return new UpdatePriceResponse(true, "Prices updated successfully.");
        } catch (Exception e) {
            return new UpdatePriceResponse(false, "Failed to update prices: " + e.getMessage());
        }
    }


}
