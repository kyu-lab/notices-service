package kyulab.noticesservice.service;

import kyulab.noticesservice.dto.gateway.UsersInfoResDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserClientService {

	private final WebClient webClient;

	@Value("${gateway.users:}")
	private String usersServicePath;

	@Value("${gateway.follow:}")
	private String followServicePath;

	public UserClientService(WebClient userServiceWebClient) {
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
