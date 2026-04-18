package io.github.lucas_goncalves_tech.tech_equipment_manager;

import org.springframework.boot.SpringApplication;

public class TestTechEquipmentManagerApplication {

	public static void main(String[] args) {
		SpringApplication.from(TechEquipmentManagerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
