package com.example.dashboard.data;


import com.example.dashboard.model.MatchOutput;
import com.example.dashboard.model.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class Listner extends JobExecutionListenerSupport {

private static final Logger log = LoggerFactory.getLogger(Listner.class);

private final EntityManager em;

@Autowired
public Listner(EntityManager em) {

    this.em = em;
}
@Transactional
@Override
public void afterJob(JobExecution jobExecution) {
    if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
        log.info("!!! JOB FINISHED! Time to verify the results");
        Map<String, Team> teamData=new HashMap<>();


em.createQuery("select u.team1,count(*) from MatchOutput u group by u.team1",Object[].class)
        .getResultList()
        .stream()
        .map(e->new Team().builder()
                        .teamName((String) e[0])
                        .totalMatch((Long) e[1])
                        .build())
        .forEach(team-> teamData.put(team.getTeamName(),team));

        em.createQuery("select u.team2,count(*) from MatchOutput u group by u.team2",Object[].class)
                .getResultList()
                .stream()
                .forEach(e->
                {
                 Team team=teamData.get((String) e[0]);
                 team.setTotalMatch(team.getTotalMatch()+(Long) e[1]);
                });
        em.createQuery("select u.MatchWinner,count(*) from MatchOutput u group by u.MatchWinner",Object[].class)
                .getResultList()
                .stream()
                .forEach(e->
                {
                    Team team=teamData.get((String) e[0]);
                    if(team !=null) {
                        team.setTotalWins((Long) e[1]);
                    }
                });

       // teamData.entrySet().stream().forEach(System.out::println);

teamData.values().forEach(e->em.persist(e));
/*em.createQuery("select u.id,teamName,totalWins,totalMatch from Team u ",Object[].class)
        .getResultList()
        .stream()
        .forEach(e-> System.out.println(e[0]+","+e[1]+","+e[2]+","+e[3]));*/
        teamData.values().forEach(System.out::println);

     /*   jdbcTemplate.query("SELECT team1,team2 FROM match_output",
                (rs, row) -> "team1"+ rs.getString(1)+"team2"+rs.getString(2)
        ).forEach(System.out::println);*/
    }
}
}