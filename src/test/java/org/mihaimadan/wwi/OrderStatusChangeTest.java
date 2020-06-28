package org.mihaimadan.wwi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mihaimadan.wwi.orders.repository.OrderRepository;
import org.mihaimadan.wwi.orders.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class OrderStatusChangeTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @Test
    public void test_order_status_change() {
        assertDoesNotThrow(() -> orderService.changeOrderStatus(75004L, "PROCESSING"));

        String updatedStatus = orderRepository.findById(75004L)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "order not found"))
                .getStatus();

        assertThat(updatedStatus)
                .isEqualTo("PROCESSING");
    }
}
