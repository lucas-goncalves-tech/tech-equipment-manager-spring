package io.github.lucas_goncalves_tech.tech_equipment_manager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class TechEquipmentManagerApplicationTests {

	@Test
	void contextLoads() {
	}

}
