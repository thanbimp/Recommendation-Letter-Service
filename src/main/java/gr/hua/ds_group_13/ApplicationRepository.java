package gr.hua.ds_group_13;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, String> {
    Optional<Application> findApplicationByAppId(String AppId);
}
