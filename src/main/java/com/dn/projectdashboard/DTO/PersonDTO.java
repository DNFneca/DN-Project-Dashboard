package com.dn.projectdashboard.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PersonDTO {

    public int id;
    public String name;
    public String position;
    public ManagerDTO manager;
    public List<PersonDTO> subordinates = new ArrayList<>();
}