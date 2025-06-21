package com.dn.projectdashboard.Mapper;

import com.dn.projectdashboard.DTO.ManagerDTO;
import com.dn.projectdashboard.DTO.PersonDTO;
import com.dn.projectdashboard.DTO.TaskDTO;
import com.dn.projectdashboard.Person.Person;
import com.dn.projectdashboard.Task.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "donePercentage", source = "donePercentage")
    TaskDTO toDto(Task task);

    List<TaskDTO> toDtoList(List<Task> task);
}