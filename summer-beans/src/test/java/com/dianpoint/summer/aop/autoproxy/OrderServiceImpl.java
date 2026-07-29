package com.dianpoint.summer.aop.autoproxy;

public class OrderServiceImpl implements OrderService {

    @Override
    public String createOrder(String item) {
        return "Order created: " + item;
    }

    @Override
    public String cancelOrder(String item) {
        return "Order cancelled: " + item;
    }
}
