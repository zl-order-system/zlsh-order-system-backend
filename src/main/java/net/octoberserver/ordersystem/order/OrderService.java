package net.octoberserver.ordersystem.order;

import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.order.dao.GetOrderDataDAO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    public Optional<GetOrderDataDAO> getOrderData() {
        List<GetOrderDataDAO.BodyData> bodyData = new ArrayList<>();

        // TODO: Use a loop to append data to list

        var headerData = new GetOrderDataDAO.HeaderData(0, 0, 0);
        return Optional.of(new GetOrderDataDAO(headerData, bodyData));
    }
}
