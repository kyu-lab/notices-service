package kyulab.noticesservice.service.gateway;

import kyulab.noticesservice.dto.gateway.UsersInfoResDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserGatewayService {

	private final WebClient webClient;

	@Value("${gateway.users-path:/users}")
	private String usersServicePath;

	@Value("${gateway.follow-path:/follow}")
	private String followServicePath;

	public UserGatewayService(WebClient userServiceWebClient) {
		this.webClient = userServiceWebClient;
	}

	public Mono<UsersInfoResDto> getUserInfo(Long userId) {
		return webClient.get()
				.uri(usersServicePath + "/{id}", userId)
				.retrieve()
				.bodyToMono(UsersInfoResDto.class);
	}

	public Flux<Long> getFollowers(Long userId) {
		return webClient.get()
				.uri(followServicePath + "/{id}", userId)
				.retrieve()
				.bodyToFlux(Long.class);
	}

}
