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
@TestPropertySource(properties = {
    "spring.flyway.enabled=false",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.datasource.url=jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
    "spring.datasource.driver-class-name=org.h2.Driver"
})
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
