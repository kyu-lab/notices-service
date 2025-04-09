package kyulab.noticesservice.controller;

import kyulab.noticesservice.document.Notices;
import kyulab.noticesservice.service.NoticesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticesController {

	private final NoticesService noticesService;

	@GetMapping(value = "/subscribe/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ServerSentEvent<String>> subscribe(@PathVariable long userId) {
		return noticesService.subscribe(userId);
	}

	@GetMapping("/{userId}")
	public Flux<Notices> getPastNotices(@PathVariable long userId) {
		return noticesService.getPastNotices(userId);
	}

	@DeleteMapping("/read/{noticesId}/notices")
	public Mono<Void> deleteNotices(@PathVariable String noticesId) {
		return noticesService.deleteNotices(noticesId);
	}

}