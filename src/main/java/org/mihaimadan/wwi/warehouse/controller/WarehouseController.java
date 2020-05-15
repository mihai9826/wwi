package org.mihaimadan.wwi.warehouse.controller;

import org.mihaimadan.wwi.warehouse.model.StockItem;
import org.mihaimadan.wwi.warehouse.model.dto.AdminStockItemDTO;
import org.mihaimadan.wwi.warehouse.model.dto.StockGroupDTO;
import org.mihaimadan.wwi.warehouse.model.dto.StockItemClientRespDTO;
import org.mihaimadan.wwi.warehouse.repository.StockGroupRepository;
import org.mihaimadan.wwi.warehouse.repository.StockItemRepository;
import org.mihaimadan.wwi.warehouse.service.StockItemService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
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

    @GetMapping("/items")
    public Page<StockItemClientRespDTO> findAllItemsPaginated(@RequestParam(required = false) String sortPrice,
                                                              @RequestParam int page,
                                                              @RequestParam int size) {
        return stockItemService.findAllItemsPaginated(sortPrice, page, size);
    }

    @GetMapping("/stock/groups")
    public List<StockGroupDTO> findAllStockGroups() {return stockItemService.findAllStockGroups();}

    @GetMapping("/stock/groups/{id}/items")
    public Page<StockItemClientRespDTO> findItemsOfStockGroup(@PathVariable Long id,
                                                              @RequestParam(required = false) String sortPrice,
                                                              @RequestParam int page, @RequestParam int size) {
        return stockItemService.findItemsOfStockGroup(id, sortPrice, page, size);
    }

    @GetMapping("/admin/stock/item/{id}")
    public AdminStockItemDTO getStockItemForAdmin(@PathVariable Long id) {
        return stockItemService.getStockItemForAdmin(id);
    }

    @PutMapping("/admin/stock/item/{id}")
    public AdminStockItemDTO updateProduct(@PathVariable Long id,
                                           @RequestBody AdminStockItemDTO editedProduct) {
        return stockItemService.updateProduct(id, editedProduct);
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
