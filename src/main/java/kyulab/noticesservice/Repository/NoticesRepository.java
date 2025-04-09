package kyulab.noticesservice.Repository;

import kyulab.noticesservice.document.Notices;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface NoticesRepository extends ReactiveMongoRepository<Notices, String> {

	Flux<Notices> findByFollowerId(Long followerId);

}
