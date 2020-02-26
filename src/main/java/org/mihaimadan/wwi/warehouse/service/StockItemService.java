package org.mihaimadan.wwi.warehouse.service;

import org.mihaimadan.wwi.warehouse.model.StockItem;
import org.mihaimadan.wwi.warehouse.model.dto.StockItemClientRespDTO;
import org.mihaimadan.wwi.warehouse.repository.StockItemRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockItemService {

   private final StockItemRepository stockItemRepository;
   private final ModelMapper modelMapper;

    public StockItemService(StockItemRepository stockItemRepository, ModelMapper modelMapper) {
        this.stockItemRepository = stockItemRepository;
        this.modelMapper = modelMapper;
    }

    public List<StockItemClientRespDTO> getFirstStockItems() {
        return stockItemRepository.findFirst20ByTaxRate(15.00).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private StockItemClientRespDTO convertToDto(StockItem stockItem) {
        StockItemClientRespDTO stockItemClientRespDTO = modelMapper.map(stockItem, StockItemClientRespDTO.class);
        int stockItemQuantity = stockItem.getStockItemHoldings().getQuantityOnHand();
        stockItemClientRespDTO.setSoldOut(stockItemQuantity < 1);
        return stockItemClientRespDTO;
    }
}
