package com.example.dashboard.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Team {
@Id
@SequenceGenerator(name = "seq",sequenceName = "seq",initialValue = 1)
@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "seq")
private Long id;
private String teamName;
private Long totalWins;
private Long totalMatch;

}
