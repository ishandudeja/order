package com.ecomerce.process.order;

import com.ecomerce.process.order.web.OrderController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OrderApplicationTests {

	@Autowired
	private OrderController controller;

	@Test
	@DisplayName("Context Loads and Controller is not Null")
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

}
