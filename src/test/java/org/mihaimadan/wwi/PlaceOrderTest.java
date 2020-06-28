package org.mihaimadan.wwi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mihaimadan.wwi.orders.model.Order;
import org.mihaimadan.wwi.orders.model.dto.OrderRequestDTO;
import org.mihaimadan.wwi.orders.repository.OrderRepository;
import org.mihaimadan.wwi.orders.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.server.ResponseStatusException;



import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class PlaceOrderTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ModelMapper modelMapper;

//    @Test
//    public void test_order_receiving() {
//        Order theOrder = orderRepository.findById(74989L)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "order not found"));
//
//        int requestedQuantity = theOrder.getOrderLines().get(0).getQuantity();
//        int lastQuantity = theOrder.getOrderLines().get(0).getStockItem().getStockItemHoldings().getQuantityOnHand();
//
//        OrderRequestDTO orderDto = modelMapper.map(theOrder, OrderRequestDTO.class);
//
//        Order newOrder = orderService.createNewOrder(orderDto);
//
//        assertEquals(lastQuantity - requestedQuantity, newOrder.getOrderLines().get(0)
//                .getStockItem().getStockItemHoldings().getQuantityOnHand());
//
//    }
}
