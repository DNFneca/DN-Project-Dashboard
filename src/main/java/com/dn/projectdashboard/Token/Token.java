package com.dn.projectdashboard.Token;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class Token {
    @Id @GeneratedValue(strategy = GenerationType.UUID)private UUID id;

    @Column(unique = true, nullable = false, length = 1000)
    String token;
    byte[] bytes;
}
