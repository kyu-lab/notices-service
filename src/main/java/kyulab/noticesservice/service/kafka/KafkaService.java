package kyulab.noticesservice.service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import kyulab.noticesservice.dto.kafka.PostDto;
import kyulab.noticesservice.service.NoticesService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaService {

	private final NoticesService noticesService;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@KafkaListener(topics = "new-post", groupId = "notices-group")
	public void consume(ConsumerRecord<String, String> record) {
		try {
			PostDto dto = objectMapper.readValue(record.value(), PostDto.class);
			noticesService.processNotification(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
