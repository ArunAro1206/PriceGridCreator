package com.assignment.price_grid;

import com.assignment.price_grid.payload.Request.CreatePriceGridRequest;
import com.assignment.price_grid.payload.Response.CreatePriceGridResponse;
import com.assignment.price_grid.payload.Response.PriceGridResponse;
import com.assignment.price_grid.services.PriceGridService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PriceGridApplicationTests {


	@Autowired
	PriceGridService priceGridService;
	@Test
	void getPriceGrid(){

		PriceGridResponse priceGridResponse= priceGridService.getPriceGrid();

		System.out.println("price : {}"+priceGridResponse.getPrices());
		System.out.println("height : {}"+priceGridResponse.getHeights());
		System.out.println("width : {}"+priceGridResponse.getWidths());
		System.out.println("ErrorCode : {}"+priceGridResponse.getErrorCode());
	}


	@Test
	void createGrid(){
		CreatePriceGridRequest createPriceGridRequest=new CreatePriceGridRequest();
		createPriceGridRequest.setPrice(100.0);
		createPriceGridRequest.setWidth(30);
		createPriceGridRequest.setHeight(50);

		CreatePriceGridResponse priceGridResponse=priceGridService.createPriceGrid(createPriceGridRequest);

		System.out.println("Message: {}"+priceGridResponse.isMsg());
		System.out.println("ErrorCode: {}"+priceGridResponse.getErrorCode());
	}
}
