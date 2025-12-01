package taskTracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taskTracker.entity.TaskGroup;
import taskTracker.entity.User;

import java.util.List;

public interface TaskGroupRepository extends JpaRepository<TaskGroup, Long> {
    List<TaskGroup> findByOwner(User owner);
}
