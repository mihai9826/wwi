package org.mihaimadan.wwi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mihaimadan.wwi.warehouse.model.StockItem;
import org.mihaimadan.wwi.warehouse.model.dto.AdminStockItemDTO;
import org.mihaimadan.wwi.warehouse.repository.StockItemRepository;
import org.mihaimadan.wwi.warehouse.service.StockItemService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class AdminEditProductTest {

    @Autowired
    private StockItemService stockItemService;

    @Autowired
    private StockItemRepository stockItemRepository;


    @Test
    public void test_stock_item_edit() {
        StockItem itemToBeUpdated = stockItemRepository.findById(2L)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "stock item not found"));

        AdminStockItemDTO itemEditRequest = new AdminStockItemDTO();
        BeanUtils.copyProperties(itemToBeUpdated, itemEditRequest, "quantityOnHand");
        itemEditRequest.setQuantityOnHand(1000);
        assertDoesNotThrow(() -> stockItemService.updateProduct(2L, itemEditRequest));

        int updatedQuantity = stockItemRepository.findById(2L)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "item not found"))
                .getStockItemHoldings().getQuantityOnHand();

        assertEquals(1000, updatedQuantity);
    }
}
