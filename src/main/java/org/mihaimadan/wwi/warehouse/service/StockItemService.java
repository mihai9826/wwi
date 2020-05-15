package org.mihaimadan.wwi.warehouse.service;

import org.mihaimadan.wwi.warehouse.model.StockItem;
import org.mihaimadan.wwi.warehouse.model.StockItemHoldings;
import org.mihaimadan.wwi.warehouse.model.StockItemStockGroup;
import org.mihaimadan.wwi.warehouse.model.dto.AdminStockItemDTO;
import org.mihaimadan.wwi.warehouse.model.dto.AdminUpdateStockItemProps;
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

import java.util.ArrayList;
import java.util.Comparator;
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

    public Page<StockItemClientRespDTO> findAllItemsPaginated(String sortPrice, int page, int size) {
        if (sortPrice != null && sortPrice.equals("ASC")) {
            return stockItemRepository.findAll(PageRequest.of(page, size, Sort.by("unitPrice")))
                    .map(this::convertToDto);
        }
        if (sortPrice != null && sortPrice.equals("DESC")) {
            return stockItemRepository.findAll(PageRequest.of(page, size, Sort.by("unitPrice").descending()))
                    .map(this::convertToDto);
        }
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

    public Page<StockItemClientRespDTO> findItemsOfStockGroup(Long id, String sortPrice, int page, int size) {
        List<StockItemStockGroup> stockItemsOfStockGroup = stockGroupRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "no stockItems found by given id"))
                .getStockItems();
        List<StockItemClientRespDTO> stockItemsDTO = new ArrayList<>();
        if (sortPrice == null) {
            stockItemsDTO = stockItemsOfStockGroup.stream()
                    .map(it -> this.convertToDto(it.getStockItem()))
                    .collect(Collectors.toList());
        }
        if (sortPrice != null && sortPrice.equals("ASC")) {
            stockItemsDTO = stockItemsOfStockGroup.stream()
                    .map(it -> this.convertToDto(it.getStockItem()))
                    .sorted(Comparator.comparingDouble(StockItemClientRespDTO::getUnitPrice))
                    .collect(Collectors.toList());
        }
        if (sortPrice != null && sortPrice.equals("DESC")) {
            stockItemsDTO = stockItemsOfStockGroup.stream()
                    .map(it -> this.convertToDto(it.getStockItem()))
                    .sorted(Comparator.comparingDouble(StockItemClientRespDTO::getUnitPrice).reversed())
                    .collect(Collectors.toList());
        }
        int start = Math.min((int) PageRequest.of(page, size).getOffset(), stockItemsDTO.size());
        int end = Math.min((start + PageRequest.of(page, size).getPageSize()), stockItemsDTO.size());

        return new PageImpl<>(stockItemsDTO.subList(start, end), PageRequest.of(page, size), stockItemsDTO.size());
    }

    private StockItemClientRespDTO convertToDto(StockItem stockItem) {
        StockItemClientRespDTO stockItemClientRespDTO = modelMapper.map(stockItem, StockItemClientRespDTO.class);
        int stockItemQuantity = stockItem.getStockItemHoldings().getQuantityOnHand();
        stockItemClientRespDTO.setSoldOut(stockItemQuantity < 1);
        return stockItemClientRespDTO;
    }

    public AdminStockItemDTO getStockItemForAdmin(Long id) {
        StockItem item = stockItemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found with id"));

        AdminStockItemDTO itemDTO = new AdminStockItemDTO();
        BeanUtils.copyProperties(item, itemDTO, "quantityOnHand");
        itemDTO.setQuantityOnHand(item.getStockItemHoldings().getQuantityOnHand());

        return itemDTO;
    }

    public AdminStockItemDTO updateProduct(Long id, AdminStockItemDTO editedProduct) {
        StockItem productToBeUpdated = stockItemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found with id"));

        BeanUtils.copyProperties(editedProduct, productToBeUpdated, "quantityOnHand");

        StockItemHoldings stockItemHoldings = productToBeUpdated.getStockItemHoldings();
        stockItemHoldings.setQuantityOnHand(editedProduct.getQuantityOnHand());
        productToBeUpdated.setStockItemHoldings(stockItemHoldings);

        stockItemRepository.save(productToBeUpdated);

        return editedProduct;
    }
}
