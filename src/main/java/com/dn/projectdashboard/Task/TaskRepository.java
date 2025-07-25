package com.dn.projectdashboard.Task;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    Integer countAllByIdNotNull();
}
