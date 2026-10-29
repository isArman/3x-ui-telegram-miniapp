package com.proxy.subscription.plan.controller;

import com.proxy.subscription.plan.domain.Plan;
import com.proxy.subscription.plan.repository.PlanRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlanPublicController {

    private final PlanRepository planRepository;

    public PlanPublicController(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @GetMapping("/plans")
    public ResponseEntity<List<Plan>> listPublicPlans() {
        return ResponseEntity.ok(planRepository.findAll());
    }
}
