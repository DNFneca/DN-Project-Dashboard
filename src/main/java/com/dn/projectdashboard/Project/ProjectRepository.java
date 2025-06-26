package com.dn.projectdashboard.Project;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    Optional<List<Project>> findAllByOwnerId(Integer ownerId);
}
