package org.mihaimadan.wwi.warehouse.controller;

import org.mihaimadan.wwi.warehouse.model.StockGroup;
import org.mihaimadan.wwi.warehouse.model.StockItem;
import org.mihaimadan.wwi.warehouse.model.dto.StockGroupDTO;
import org.mihaimadan.wwi.warehouse.model.dto.StockItemClientRespDTO;
import org.mihaimadan.wwi.warehouse.repository.StockGroupRepository;
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
    private final StockGroupRepository stockGroupRepository;
    private final StockItemService stockItemService;

    public WarehouseController(StockItemService stockItemService, StockItemRepository stockItemRepository,
                 StockGroupRepository stockGroupRepository) {
        this.stockItemService = stockItemService;
        this.stockItemRepository = stockItemRepository;
        this.stockGroupRepository = stockGroupRepository;
    }

    @GetMapping("/")
    public List<StockItemClientRespDTO> getFirstStockItemsElements() {
        return stockItemService.getFirstStockItems();
    }

    @GetMapping("/stock/groups")
    public List<StockGroupDTO> findAllStockGroups() {return stockItemService.findAllStockGroups();}

    @GetMapping("/{id}")
    public StockGroup getStockItem(@PathVariable Long id) {
        return stockGroupRepository.findById(id)
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
