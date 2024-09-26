package com.fleet.status;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class FleetStatusApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void failedTest() {
		fail("This is a failed test.");
	}

}
