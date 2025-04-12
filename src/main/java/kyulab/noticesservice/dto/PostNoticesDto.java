package kyulab.noticesservice.dto;

import kyulab.noticesservice.entity.Notices;

import java.time.LocalDateTime;

public record PostNoticesDto(
		String noticesId,
		long postId,
		long userId,
		String name,
		String subject,
		LocalDateTime noticesAt) {
	public static PostNoticesDto from(Notices notices) {
		return new PostNoticesDto(
				notices.getId(),
				notices.getPostId(),
				notices.getUserId(),
				notices.getUsername(),
				notices.getSubject(),
				notices.getNoticesAt()
		);
	}
}
