package kyulab.noticesservice.document;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Document(collection = "notices")
public class Notices {
	@Id
	private String id;

	private final long postId;

	private final long userId;

	private final String username;

	private final long followerId;

	private final String subject;

	private final String type;

	private final LocalDateTime noticesAt;

	@Indexed(expireAfter = "7d") // 7일 후 자동삭제
	private final Instant timestamp;

	public Notices(long postId, long userId, String username, long followerId, String subject, String type) {
		this.postId = postId;
		this.userId = userId;
		this.username = username;
		this.followerId = followerId;
		this.subject = subject;
		this.type = type;
		this.noticesAt = LocalDateTime.now();
		this.timestamp = Instant.now();
	}

}
