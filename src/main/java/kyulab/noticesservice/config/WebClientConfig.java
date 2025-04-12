package kyulab.noticesservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

	@Value("${gateway.base-url:}")
	private String baseUrl;

	@Value("${gateway.gateway-key:}")
	private String key;

	@Bean
	public WebClient userFollowServiceWebClient() {
		return WebClient.builder()
				.baseUrl(baseUrl)
				.defaultHeader("X-GATE-WAY-KEY", key)
				.build();
	}

}
