package kyulab.noticesservice.dto.kafka;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDto {
	private long postId;
	private long userId;
	private String subject;
	private String type;

	public PostDto(long postId, long userId, String subject, String type) {
		this.postId = postId;
		this.userId = userId;
		this.subject = subject;
		this.type = type;
	}
}
