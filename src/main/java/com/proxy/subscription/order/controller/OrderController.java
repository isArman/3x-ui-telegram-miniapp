package com.proxy.subscription.order.controller;

import com.proxy.subscription.auth.authorization.AdminAuthorization;
import com.proxy.subscription.auth.service.TelegramAuthService;
import com.proxy.subscription.order.domain.Order;
import com.proxy.subscription.order.repository.OrderRepository;
import com.proxy.subscription.plan.repository.PlanRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final PlanRepository planRepository;
    private final TelegramAuthService telegramAuthService;
    private final AdminAuthorization adminAuthorization;

    public OrderController(OrderRepository orderRepository,
                            PlanRepository planRepository,
                            TelegramAuthService telegramAuthService,
                            AdminAuthorization adminAuthorization) {
        this.orderRepository = orderRepository;
        this.planRepository = planRepository;
        this.telegramAuthService = telegramAuthService;
        this.adminAuthorization = adminAuthorization;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(
            @RequestHeader(value = "X-Telegram-Init-Data", required = false) String initData,
            @RequestHeader(value = "X-Telegram-Bot-Token", required = false) String botToken,
            @RequestBody Map<String, Object> payload) {
        // Validate Telegram identity
        if (initData == null || botToken == null || !telegramAuthService.validateInitData(initData, botToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long telegramUserId = telegramAuthService.extractUserId(initData);
        if (telegramUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long planId = ((Number) payload.get("planId")).longValue();
        var plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        Order order = new Order(
                telegramUserId,
                plan.getId(),
                plan.getName(),
                plan.getDataAllowanceGb(),
                plan.getValidityDurationDays(),
                plan.getPriceToman()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(orderRepository.save(order));
    }

    @GetMapping("/admin/orders")
    public ResponseEntity<java.util.List<Order>> listAdminOrders(
            @RequestHeader(value = "X-Telegram-User-Id", required = false) Long userId) {
        adminAuthorization.enforceAdmin(userId);
        return ResponseEntity.ok(orderRepository.findAll());
    }
}
