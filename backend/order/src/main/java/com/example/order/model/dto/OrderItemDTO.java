package com.example.order.model.dto;

import java.math.BigDecimal;

public record OrderItemDTO(Integer id, Integer quantity, BigDecimal price, Integer orderId, Integer productId) {
}
