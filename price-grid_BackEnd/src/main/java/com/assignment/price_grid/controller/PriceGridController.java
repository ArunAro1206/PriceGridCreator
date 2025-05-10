package com.assignment.price_grid.controller;

import com.assignment.price_grid.model.PriceCell;
import com.assignment.price_grid.payload.Request.CreatePriceGridRequest;
import com.assignment.price_grid.payload.Response.CreatePriceGridResponse;
import com.assignment.price_grid.payload.Response.PriceGridResponse;
import com.assignment.price_grid.services.PriceGridService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PriceGridController {

    @Autowired
    private PriceGridService priceGridService;

    @GetMapping("/getPriceDetails")
    public PriceGridResponse getGrid() {
        return priceGridService.getPriceGrid();
    }

    @PostMapping("/createPriceGrid")
    public ResponseEntity<?> createPriceGrid(@RequestBody CreatePriceGridRequest createPriceGridRequest) {
       CreatePriceGridResponse priceGridResponse= priceGridService.createPriceGrid(createPriceGridRequest);
        return ResponseEntity.ok(priceGridResponse);
    }


    @PostMapping("/updatePriceGrid")
    public ResponseEntity<?> updatePrices(@RequestBody List<PriceCell> updatedEntries) {
        priceGridService.updatePrices(updatedEntries);
        return ResponseEntity.ok("Saved");
    }
}
