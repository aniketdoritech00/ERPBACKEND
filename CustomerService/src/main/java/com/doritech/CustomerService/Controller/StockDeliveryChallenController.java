package com.doritech.CustomerService.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Request.StockDeliveryChallanRequest;
import com.doritech.CustomerService.Service.StockDeliveryChallanService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/stock/delivery")
public class StockDeliveryChallenController {

	@Autowired
	private StockDeliveryChallanService stockDeliveryChallenService;

	@PostMapping("/saveStockDeliveryChallen")
	public ResponseEntity saveStockDeliveryChallen(
			@RequestBody @Valid StockDeliveryChallanRequest request) {
		return stockDeliveryChallenService.saveStockDelivery(request);
	}

	@GetMapping("/getAllStockDeliveryChallen")
	public ResponseEntity getAllStockDeliveryChallen(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		return stockDeliveryChallenService.getAllStockDelivery(page, size);
	}

	@GetMapping("/getStockDeliveryById")
	public ResponseEntity getStockDeliveryById(@RequestParam Integer id) {
		return stockDeliveryChallenService.getStockDeliveryById(id);
	}

	@DeleteMapping("/multipleDeleteStokeDelivery")
	public ResponseEntity multipleDeleteStokeDelivery(List<Integer> ids) {
		return stockDeliveryChallenService.deleteMultipleStockDelivery(ids);
	}

}
