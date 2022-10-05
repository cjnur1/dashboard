package com.example.dashboard.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor

public class Team {
@Id
private Long id;
private String teamName;
private Long totalWins;
private Long totalMatchs;

public Team(String teamName, Long totalWins) {
    this.teamName = teamName;
    this.totalWins = totalWins;

}
}
