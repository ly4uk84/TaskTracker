package taskTracker.controller;

import taskTracker.entity.TaskGroup;
import taskTracker.entity.User;
import taskTracker.repository.TaskGroupRepository;
import taskTracker.repository.UserRepository;
import taskTracker.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task-groups")
@RequiredArgsConstructor
public class GroupController {

    private final TaskGroupRepository taskGroupRepository;
    private final UserRepository userRepository;

    // Получить свои группы задач
    @GetMapping
    public List<TaskGroup> getMyGroups(@AuthenticationPrincipal UserDetails userDetails) {
        User owner = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        return taskGroupRepository.findByOwner(owner);
    }

    // Создать новую группу
    @PostMapping
    public ResponseEntity<TaskGroup> createGroup(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CreateGroupRequest request) {
        User owner = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();

        TaskGroup group = new TaskGroup();
        group.setName(request.name());
        group.setOwner(owner);

        TaskGroup saved = taskGroupRepository.save(group);
        return ResponseEntity.ok(saved);
    }

    // Переименовать группу
    @PutMapping("/{groupId}")
    public ResponseEntity<TaskGroup> updateGroup(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long groupId,
            @RequestBody UpdateGroupRequest request) {
        TaskGroup group = taskGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        // Проверка владельца
        User owner = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        if (!group.getOwner().equals(owner)) {
            return ResponseEntity.status(403).build();
        }

        group.setName(request.name());
        return ResponseEntity.ok(taskGroupRepository.save(group));
    }

    // Удалить группу
    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteGroup(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long groupId) {
        TaskGroup group = taskGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        User owner = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        if (!group.getOwner().equals(owner)) {
            return ResponseEntity.status(403).build();
        }

        taskGroupRepository.delete(group);
        return ResponseEntity.noContent().build();
    }
}

// DTO
record CreateGroupRequest(String name) {}
record UpdateGroupRequest(String name) {}

