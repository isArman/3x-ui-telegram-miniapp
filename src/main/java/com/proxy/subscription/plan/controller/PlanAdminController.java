package com.proxy.subscription.plan.controller;

import com.proxy.subscription.auth.authorization.AdminAuthorization;
import com.proxy.subscription.plan.domain.Plan;
import com.proxy.subscription.plan.repository.PlanRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/plans")
public class PlanAdminController {

    private final PlanRepository planRepository;
    private final AdminAuthorization adminAuthorization;

    public PlanAdminController(PlanRepository planRepository, AdminAuthorization adminAuthorization) {
        this.planRepository = planRepository;
        this.adminAuthorization = adminAuthorization;
    }

    @GetMapping
    public ResponseEntity<List<Plan>> listPlans(@RequestHeader(value = "X-Telegram-User-Id", required = false) Long userId) {
        adminAuthorization.enforceAdmin(userId);
        return ResponseEntity.ok(planRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Plan> createPlan(
            @RequestHeader(value = "X-Telegram-User-Id", required = false) Long userId,
            @RequestBody Map<String, Object> payload) {
        adminAuthorization.enforceAdmin(userId);
        Plan plan = new Plan(
                (String) payload.get("name"),
                ((Number) payload.get("dataAllowanceGb")).longValue(),
                ((Number) payload.get("validityDurationDays")).intValue(),
                new BigDecimal(payload.get("priceToman").toString())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(planRepository.save(plan));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Plan> updatePlan(
            @RequestHeader(value = "X-Telegram-User-Id", required = false) Long userId,
            @PathVariable Long id,
            @RequestBody Map<String, Object> payload) {
        adminAuthorization.enforceAdmin(userId);
        Plan existing = planRepository.findById(id).orElseThrow(() -> new RuntimeException("Plan not found"));
        existing.setName((String) payload.get("name"));
        existing.setDataAllowanceGb(((Number) payload.get("dataAllowanceGb")).longValue());
        existing.setValidityDurationDays(((Number) payload.get("validityDurationDays")).intValue());
        existing.setPriceToman(new BigDecimal(payload.get("priceToman").toString()));
        return ResponseEntity.ok(planRepository.save(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(
            @RequestHeader(value = "X-Telegram-User-Id", required = false) Long userId,
            @PathVariable Long id) {
        adminAuthorization.enforceAdmin(userId);
        planRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
