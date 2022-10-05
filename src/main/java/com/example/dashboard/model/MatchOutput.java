package com.example.dashboard.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@ToString
public class MatchOutput {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
private String city;
private LocalDate date;
private String playerOfMatch;
private String venue;
private String team1;
private String team2;
private String tossWinner;
private String tossDecision;
private String MatchWinner;
private String result;
private String resultMargin;
private String umpire1;
private String umpire2;
}
