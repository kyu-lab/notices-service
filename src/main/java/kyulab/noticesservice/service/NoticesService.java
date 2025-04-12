package kyulab.noticesservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kyulab.noticesservice.Repository.NoticesRepository;
import kyulab.noticesservice.dto.PostNoticesDto;
import kyulab.noticesservice.dto.kafka.PostDto;
import kyulab.noticesservice.entity.Notices;
import kyulab.noticesservice.service.gateway.UserGatewayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticesService {

	private final ObjectMapper objectMapper = new ObjectMapper();
	private final Map<Long, Sinks.Many<ServerSentEvent<String>>> userSinks = new ConcurrentHashMap<>();
	private final UserGatewayService userGatewayService;
	private final NoticesRepository noticesRepository;

	public Flux<ServerSentEvent<String>> subscribe(long userId) {
		Sinks.Many<ServerSentEvent<String>> sink = Sinks.many().multicast().onBackpressureBuffer();
		userSinks.put(userId, sink);

		// 즉시 연결을 위한 설정
		Flux<ServerSentEvent<String>> initialEvent = Flux.just(
				ServerSentEvent.<String>builder()
						.event("ping")
						.data("ping")
						.build()
		);

		// 하트비트. 30초 단위로 발송
		Flux<ServerSentEvent<String>> heartbeat = Flux.interval(Duration.ofSeconds(30))
				.map(ping -> ServerSentEvent.<String>builder()
						.event("ping")
						.data("ping")
						.build());

		return Flux.merge(initialEvent, sink.asFlux(), heartbeat)
				.doFinally(s -> {
					log.debug("유저 {} 연결 종료", userId);
					userSinks.remove(userId);
				});
	}

	public void processNotification(PostDto postDto) {
		userGatewayService.getUserInfo(postDto.getUserId())
			.flatMapMany(userInfo -> userGatewayService.getFollowers(postDto.getUserId())
				.flatMap(followerId -> {
					Notices notices = new Notices(
							postDto.getPostId(),
							postDto.getUserId(),
							userInfo.name(),
							followerId,
							postDto.getSubject(),
							postDto.getType()
					);
					return noticesRepository.save(notices);
				})
				.doOnNext(notices -> {
					Sinks.Many<ServerSentEvent<String>> sink = userSinks.get(notices.getFollowerId());
					if (sink != null) {
						PostNoticesDto postNoticesDto = PostNoticesDto.from(notices);
						try {
							String message = objectMapper.writeValueAsString(postNoticesDto);
							ServerSentEvent<String> newPostEvent = ServerSentEvent.<String>builder()
									.event("notices")
									.data(message)
									.build();
							Sinks.EmitResult result = sink.tryEmitNext(newPostEvent);
							if (result.isFailure()) {
								log.warn("팔로우 {} 알림 전송 실패", notices.getFollowerId());
							}
						} catch (JsonProcessingException e) {
							log.error("알림 데이터 파싱 실패 : {}, 에러 내용 : {}", postNoticesDto, e.getMessage());
							throw new RuntimeException(e);
						}
					}
				}))
			.doOnError(error -> log.error("Error: {}", error.getMessage()))
			.subscribe();
	}

	public Flux<Notices> getPastNotices(long userId) {
		log.debug("유저 {}가 알림 내용 확인시도", userId);
		return noticesRepository.findByFollowerId(userId);
	}

	public Mono<Void> deleteNotices(String noticesId) {
		log.debug("알림 {} 삭제", noticesId);
		return noticesRepository.deleteById(noticesId);
	}

}
