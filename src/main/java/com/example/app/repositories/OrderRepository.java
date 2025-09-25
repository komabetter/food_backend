package com.example.app.repositories;

import java.util.ArrayList;
import java.util.List;

import com.example.app.models.OrderModel;

public class OrderRepository {

        private List<OrderModel> orders = new ArrayList<>(
                        List.of(
                                        new OrderModel(1, "OrderNow", "Detail", (float) 32.0, "sert"),
                                        new OrderModel(2, "TEST", "Detail", (float) 33.03, "Moku")

                        ));

        public List<OrderModel> getOrders() {
                return orders;
        }
}
