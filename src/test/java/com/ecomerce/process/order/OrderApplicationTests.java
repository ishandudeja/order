package com.ecomerce.process.order;

import com.ecomerce.process.order.domain.Order;
import com.ecomerce.process.order.repository.OrderRepository;
import com.ecomerce.process.order.service.JwtService;
import com.ecomerce.process.order.service.OrderService;
import com.ecomerce.process.order.web.OrderController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.security.access.prepost.PreAuthorize;

class OrderApplicationTests {

	@Test
	@DisplayName("Controller constructs and is not Null")
	void controllerConstructs() {
		OrderRepository repo = mock(OrderRepository.class);
		OrderController controller = new OrderController(repo);
		assertThat(controller).isNotNull();
	}

	@Test
	@DisplayName("OrderController.countOrders delegates to repository")
	public void countOrders_delegatesToRepository() {
		OrderRepository repo = mock(OrderRepository.class);
		when(repo.count()).thenReturn(7L);
		OrderController controller = new OrderController(repo);
		Long cnt = controller.countOrders();
		assertThat(cnt).isEqualTo(7L);
	}

	@Test
	@DisplayName("OrderController.getAllOrders delegates to repository")
	public void getAllOrders_delegatesToRepository() {
		Order order = new Order("Alice","alice@example.com","123", Collections.emptyList());
		OrderRepository repo = mock(OrderRepository.class);
		when(repo.findAllWithItems()).thenReturn(Collections.singletonList(order));
		OrderController controller = new OrderController(repo);
		Iterable<Order> result = controller.getAllOrders();
		assertThat(result).contains(order);
	}

	@Test
	@DisplayName("OrderService methods have correct PreAuthorize annotations")
	public void orderService_hasPreAuthorizeAnnotations() throws NoSuchMethodException {
		Method update = OrderService.class.getMethod("updateOrder", Order.class);
		PreAuthorize paUpdate = update.getAnnotation(PreAuthorize.class);
		if (paUpdate == null) {
			paUpdate = OrderService.class.getAnnotation(PreAuthorize.class);
		}
		assertThat(paUpdate).isNotNull();
		assertThat(paUpdate.value()).contains("hasRole('USER')");

		Method delete = OrderService.class.getMethod("deleteOrder", Long.class);
		PreAuthorize paDelete = delete.getAnnotation(PreAuthorize.class);
		if (paDelete == null) {
			paDelete = OrderService.class.getAnnotation(PreAuthorize.class);
		}
		assertThat(paDelete).isNotNull();
		assertThat(paDelete.value()).contains("hasRole('ADMIN')");
	}

	@Test
	@DisplayName("JwtService token generation and auth extraction")
	public void jwtService_tokenRoundTrip() {
		JwtService jwtService = new JwtService();
		String username = "dan";
		String token = jwtService.getToken(username);
		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer" + token);
		String extracted = jwtService.getAuthUser(req);
		assertThat(extracted).isEqualTo(username);
	}

}
