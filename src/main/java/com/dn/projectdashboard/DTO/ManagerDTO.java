package com.dn.projectdashboard.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ManagerDTO {
    public Long id;
    public String name;
    public String position;
    public ManagerDTO manager;

}
