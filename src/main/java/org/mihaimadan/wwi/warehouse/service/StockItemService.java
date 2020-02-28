package org.mihaimadan.wwi.warehouse.service;

import org.mihaimadan.wwi.warehouse.model.StockGroup;
import org.mihaimadan.wwi.warehouse.model.StockItem;
import org.mihaimadan.wwi.warehouse.model.dto.StockGroupDTO;
import org.mihaimadan.wwi.warehouse.model.dto.StockItemClientRespDTO;
import org.mihaimadan.wwi.warehouse.repository.StockGroupRepository;
import org.mihaimadan.wwi.warehouse.repository.StockItemRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.beans.BeanUtils.copyProperties;

@Service
public class StockItemService {

   private final StockItemRepository stockItemRepository;
   private final StockGroupRepository stockGroupRepository;
   private final ModelMapper modelMapper;

    public StockItemService(StockItemRepository stockItemRepository, StockGroupRepository stockGroupRepository, ModelMapper modelMapper) {
        this.stockItemRepository = stockItemRepository;
        this.stockGroupRepository = stockGroupRepository;
        this.modelMapper = modelMapper;
    }

    public List<StockItemClientRespDTO> getFirstStockItems() {
        return stockItemRepository.findFirst20ByTaxRate(15.00).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<StockGroupDTO> findAllStockGroups() {
        return stockGroupRepository.findAll(Sort.by("stockGroupId")).stream()
                .map(it -> {
                    StockGroupDTO stockGroupDTO = new StockGroupDTO();
                    BeanUtils.copyProperties(it, stockGroupDTO, "stockItems");
                    return stockGroupDTO;
                })
                .collect(Collectors.toList());
    }

    private StockItemClientRespDTO convertToDto(StockItem stockItem) {
        StockItemClientRespDTO stockItemClientRespDTO = modelMapper.map(stockItem, StockItemClientRespDTO.class);
        int stockItemQuantity = stockItem.getStockItemHoldings().getQuantityOnHand();
        stockItemClientRespDTO.setSoldOut(stockItemQuantity < 1);
        return stockItemClientRespDTO;
    }
}
