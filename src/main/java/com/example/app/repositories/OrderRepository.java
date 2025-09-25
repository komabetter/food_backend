package com.example.app.repositories;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.example.app.models.OrderModel;

@Repository
public class OrderRepository {

        private List<OrderModel> orders = new ArrayList<>(
                        List.of(
                                        new OrderModel("UUID2", 1, "OrderNow", "Detail", (float) 32.0, "sert"),
                                        new OrderModel("UUID1", 2, "TEST", "Detail", (float) 33.03, "Moku")

                        ));

        public List<OrderModel> getOrders() {
                return orders;
        }

        public OrderModel addOrder(OrderModel order) {
                orders.add(order);
                return order;
        }

        public void editOrder(OrderModel order) {
                for (int i = 0; i < orders.size(); i++) {
                        if (orders.get(i).getId().equals(order.getId())) {
                                // orders.set(i, order);
                                orders.get(i).setOrderStatusId(order.getOrderStatusId());
                                orders.get(i).setOrderStatusName(order.getOrderStatusName());

                                return;
                        }
                }
        }
}
