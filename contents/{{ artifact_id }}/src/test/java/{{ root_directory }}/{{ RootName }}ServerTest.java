package {{ root_package }};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;

//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
//		properties = {"httpbin=http://localhost:${wiremock.server.port}"},
//		classes = {{ RootName }}Config.class
//)
//@AutoConfigureWireMock(port = 0)
public class {{ RootName }}ServerTest {

	@Autowired
	private WebTestClient webClient;

//	@Test
	public void contextLoads() throws Exception {
		//Stubs
		stubFor(get(urlEqualTo("/get"))
				.willReturn(aResponse()
					.withBody("{\"headers\":{\"Hello\":\"World\"}}")
					.withHeader("Content-Type", "application/json")));
		stubFor(get(urlEqualTo("/delay/3"))
			.willReturn(aResponse()
				.withBody("no fallback")
				.withFixedDelay(3000)));

		webClient
			.get().uri("/get")
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.headers.Hello").isEqualTo("World");

		webClient
			.get().uri("/delay/3")
			.header("Host", "www.hystrix.com")
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.consumeWith(
				response -> assertThat(response.getResponseBody()).isEqualTo("fallback".getBytes()));
	}
}