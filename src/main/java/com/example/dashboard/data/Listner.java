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

@Override
public void afterJob(JobExecution jobExecution) {
    if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
        log.info("!!! JOB FINISHED! Time to verify the results");
        Map<String, Team> teamData=new HashMap<>();

em.createQuery("select  u.team1,count(*) from MatchOutput u group by u.team1",Object[].class)
        .getResultList().stream()
        .map(e->new Team((String)e[0],(Long)e[1]))
        .forEach(t->System.out.println(t.getTeamName()+"---wins--"+t.getTotalWins()));

     /*   jdbcTemplate.query("SELECT team1,team2 FROM match_output",
                (rs, row) -> "team1"+ rs.getString(1)+"team2"+rs.getString(2)
        ).forEach(System.out::println);*/
    }
}
}