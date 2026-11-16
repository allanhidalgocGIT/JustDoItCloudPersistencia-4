package teccr.justdoitcloud.repository;

import org.springframework.data.repository.CrudRepository;
import teccr.justdoitcloud.data.Task;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Long> {

    List<Task> findByUserId(Long userId);
}