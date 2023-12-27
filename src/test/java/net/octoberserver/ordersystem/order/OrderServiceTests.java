package net.octoberserver.ordersystem.order;


import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import net.octoberserver.ordersystem.order.dao.GetOrderDataDAO;
import org.aspectj.weaver.ast.Or;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

public class OrderServiceTests {

    @Mock
    private OrderRepository orderRepository;

    private OrderService underTest;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        underTest = new OrderService(orderRepository);
    }
    @Test
    void getOrderData() {
        long userID = 11230157L;

        // when
        when(orderRepository.findUpcomingMealsWithOrders(userID)).thenReturn(Arrays.asList());
        var data = underTest.getOrderData(userID);
        // then
        verify(orderRepository).findUpcomingMealsWithOrders(userID);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}
