package org.mihaimadan.wwi.warehouse.controller;

import org.mihaimadan.wwi.warehouse.model.StockItem;
import org.mihaimadan.wwi.warehouse.model.dto.StockItemClientRespDTO;
import org.mihaimadan.wwi.warehouse.repository.StockItemRepository;
import org.mihaimadan.wwi.warehouse.service.StockItemService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/warehouse")
public class WarehouseController {
    private final StockItemRepository stockItemRepository;
    private final StockItemService stockItemService;

    public WarehouseController(StockItemService stockItemService, StockItemRepository stockItemRepository) {
        this.stockItemService = stockItemService;
        this.stockItemRepository = stockItemRepository;
    }

    @GetMapping("/")
    public List<StockItemClientRespDTO> getFirstStockItemsElements() {
        return stockItemService.getFirstStockItems();
    }

    @GetMapping("/{id}")
    public StockItem getStockItem(@PathVariable Long id) {
        return stockItemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found by given id"));
    }

    @PutMapping("/{id}")
    public StockItem updateStockItem(@PathVariable Long id, @RequestBody StockItem updatedItem) {
        StockItem existing = stockItemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found"));

        BeanUtils.copyProperties(updatedItem, existing);
        stockItemRepository.save(existing);

        return existing;

    }

}
