package com.dn.projectdashboard.Team;

import com.dn.projectdashboard.Person.PersonNotFoundException;
import com.dn.projectdashboard.Person.PersonRepository;
import com.dn.projectdashboard.Sprint.SprintNotFoundException;
import com.dn.projectdashboard.Sprint.SprintRepository;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@AllArgsConstructor
@Controller
public class TeamController {

    private PersonRepository personRepository;
    private SprintRepository sprintRepository;
    private TeamRepository repository;

    @MutationMapping
    public Team newTeam(@Argument String name, @Argument String description, @Argument Integer teamLeadId, @Argument Integer sprintId, @Argument List<Integer> memberIds) {
        Team team = new Team();
        team.setName(name);
        team.setDescription(description);
        team.setTeamLead(personRepository.findById(teamLeadId).orElseThrow(() -> new PersonNotFoundException(teamLeadId)));
        team.setMembers(personRepository.findAllById(memberIds));
        team.setSprint(sprintRepository.findById(sprintId).orElseThrow(() -> new SprintNotFoundException("Sprint not found")));
        return repository.save(team);
    }

}
