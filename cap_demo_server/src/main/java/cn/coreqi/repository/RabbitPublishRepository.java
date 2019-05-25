package cn.coreqi.repository;

import cn.coreqi.entities.RabbitPublished;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RabbitPublishRepository extends JpaRepository<RabbitPublished,String> {
    List<RabbitPublished> findByStatusName(String StatusName);
}
