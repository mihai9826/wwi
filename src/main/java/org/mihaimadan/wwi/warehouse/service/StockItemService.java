package org.mihaimadan.wwi.warehouse.service;

import org.mihaimadan.wwi.warehouse.model.StockItem;
import org.mihaimadan.wwi.warehouse.model.StockItemStockGroup;
import org.mihaimadan.wwi.warehouse.model.dto.StockGroupDTO;
import org.mihaimadan.wwi.warehouse.model.dto.StockItemClientRespDTO;
import org.mihaimadan.wwi.warehouse.repository.StockGroupRepository;
import org.mihaimadan.wwi.warehouse.repository.StockItemRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


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

    public Page<StockItemClientRespDTO> findAllItemsPaginated(int page, int size) {
        return stockItemRepository.findAll(PageRequest.of(page, size))
                .map(this::convertToDto);
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

    public Page<StockItemClientRespDTO> findItemsOfStockGroup(Long id, int page, int size) {
        List<StockItemStockGroup> stockItemsOfStockGroup = stockGroupRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "no stockItems found by given id"))
                .getStockItems();
        List<StockItemClientRespDTO> stockItemsDTO = stockItemsOfStockGroup.stream()
                .map(it -> this.convertToDto(it.getStockItem()))
                .collect(Collectors.toList());

        int start = Math.min((int)PageRequest.of(page, size).getOffset(), stockItemsDTO.size());
        int end = Math.min((start + PageRequest.of(page, size).getPageSize()), stockItemsDTO.size());

        return new PageImpl<>(stockItemsDTO.subList(start, end), PageRequest.of(page, size), stockItemsDTO.size());
    }

    private StockItemClientRespDTO convertToDto(StockItem stockItem) {
        StockItemClientRespDTO stockItemClientRespDTO = modelMapper.map(stockItem, StockItemClientRespDTO.class);
        int stockItemQuantity = stockItem.getStockItemHoldings().getQuantityOnHand();
        stockItemClientRespDTO.setSoldOut(stockItemQuantity < 1);
        return stockItemClientRespDTO;
    }
}
