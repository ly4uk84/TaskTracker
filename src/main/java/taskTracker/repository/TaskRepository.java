package taskTracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import taskTracker.entity.Task;
import taskTracker.entity.TaskGroup;
import taskTracker.entity.User;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByOwner(User owner);
    List<Task> findByGroup(TaskGroup group);

    @Query("SELECT t.status, COUNT(t) FROM Task t GROUP BY t.status")
    List<Object[]> countByStatus();

    @Query("SELECT t.owner.id, COUNT(t) FROM Task t GROUP BY t.owner.id")
    List<Object[]> findByOwnerIdCount();

    List<Task> findByOwnerId(Long ownerId);
}
