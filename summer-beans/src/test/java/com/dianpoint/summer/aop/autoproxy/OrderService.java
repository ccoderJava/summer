package com.dianpoint.summer.aop.autoproxy;

public interface OrderService {
    String createOrder(String item);
    String cancelOrder(String item);
}
