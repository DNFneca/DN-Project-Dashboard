package com.dn.projectdashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectDashboardApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjectDashboardApplication.class, args);
    }
}

//  TODO: Fix some of the return values and maybe make DTO's for all of the objects so that passwords and id's can't be pulled everywhere.
// TODO: Optimize, fix!


// TODO: Add security, complete the http only refreshToken and add it to the response... intermediate push