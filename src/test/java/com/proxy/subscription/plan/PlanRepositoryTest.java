package com.proxy.subscription.plan;

import com.proxy.subscription.plan.domain.Plan;
import com.proxy.subscription.plan.repository.PlanRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1")
class PlanRepositoryTest {

    @Autowired
    private PlanRepository planRepository;

    @Test
    void shouldSaveAndRetrievePlan() {
        Plan plan = new Plan("Basic", 10L, 30, new BigDecimal("50000"));
        Plan saved = planRepository.save(plan);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Basic");
    }
}
