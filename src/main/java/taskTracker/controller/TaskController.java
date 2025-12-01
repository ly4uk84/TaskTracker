package taskTracker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import taskTracker.entity.Status;
import taskTracker.entity.Task;
import taskTracker.entity.TaskGroup;
import taskTracker.entity.User;
import taskTracker.repository.TaskGroupRepository;
import taskTracker.repository.TaskRepository;
import taskTracker.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskGroupRepository taskGroupRepository;

    // 1. Список своих задач
    @GetMapping
    public List<Task> getMyTasks(@AuthenticationPrincipal UserDetails userDetails) {
        User owner = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        return taskRepository.findByOwner(owner);
    }

    // 2. Создать задачу
    @PostMapping
    public ResponseEntity<Task> createTask(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CreateTaskRequest request) {
        User owner = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();

        Task task = new Task();
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(Status.PLAN);
        task.setOwner(owner);

        // Привязка к группе (опционально)
        if (request.groupId() != null) {
            TaskGroup group = taskGroupRepository.findById(request.groupId())
                    .orElseThrow(() -> new RuntimeException("Group not found"));
            task.setGroup(group);
        }

        Task saved = taskRepository.save(task);
        return ResponseEntity.ok(saved);
    }

    // 3. Получить задачу по ID
    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTask(@PathVariable Long taskId) {
        return taskRepository.findById(taskId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. Изменить задачу
    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long taskId,
            @RequestBody UpdateTaskRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User owner = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        if (!task.getOwner().equals(owner)) {
            return ResponseEntity.status(403).build();
        }

        task.setTitle(request.title());
        task.setDescription(request.description());

        // Изменение статуса
        if (request.status() != null) {
            task.setStatus(request.status());
        }

        // Изменение группы
        if (request.groupId() != null) {
            TaskGroup group = taskGroupRepository.findById(request.groupId())
                    .orElseThrow(() -> new RuntimeException("Group not found"));
            task.setGroup(group);
        }

        return ResponseEntity.ok(taskRepository.save(task));
    }

    // 5. Удалить задачу
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User owner = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        if (!task.getOwner().equals(owner)) {
            return ResponseEntity.status(403).build();
        }

        taskRepository.delete(task);
        return ResponseEntity.noContent().build();
    }
}

record CreateTaskRequest(String title, String description, Long groupId) {
}

record UpdateTaskRequest(String title, String description, Status status, Long groupId) {
}
