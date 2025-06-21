package com.dn.projectdashboard.Task;

import com.dn.projectdashboard.DTO.TaskDTO;
import com.dn.projectdashboard.Mapper.TaskMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
class TaskController {

    private final TaskRepository repository;
    private final TaskMapper taskMapper;

    TaskController(TaskRepository repository, TaskMapper taskMapper) {
        this.repository = repository;
        this.taskMapper = taskMapper;
    }


    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/tasks")
    List<TaskDTO> all() {
        List<Task> all = repository.findAll();
        return taskMapper.toDtoList(all);
    }
    // end::get-aggregate-root[]

    @PostMapping("/tasks")
    Task newTask(@RequestBody Task newTask) {
        return repository.save(newTask);
    }

    // Single item

    @GetMapping("/tasks/{id}")
    Task one(@PathVariable Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    @PutMapping("/tasks/{id}")
    Task replaceTask(@RequestBody Task newTask, @PathVariable Long id) {

        return repository.findById(id)
                .map(employee -> {
                    employee.setTitle(newTask.getTitle());
                    return repository.save(employee);
                })
                .orElseGet(() -> {
                    return repository.save(newTask);
                });
    }

    @DeleteMapping("/tasks/{id}")
    void deleteTask(@PathVariable Long id) {
        repository.deleteById(id);
    }
}