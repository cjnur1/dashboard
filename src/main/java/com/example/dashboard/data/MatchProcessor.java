package com.example.dashboard.data;


import com.example.dashboard.model.MatchOutput;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class MatchProcessor implements ItemProcessor<MatchInput, MatchOutput> {

@Override
public MatchOutput process(MatchInput matchInput) throws Exception {
       MatchOutput match=new MatchOutput();

       String firstTeam,secondTeam;
    if("bat".equalsIgnoreCase(matchInput.getToss_decision()))
    {
        firstTeam =matchInput.getToss_winner();
        secondTeam=matchInput.getToss_winner().equals(matchInput.getTeam1())
                           ?matchInput.getTeam1(): matchInput.getTeam2();
    }
    else
    {
        secondTeam=matchInput.getToss_winner();
        firstTeam=matchInput.getToss_winner().equals(matchInput.getTeam1())
                          ?matchInput.getTeam1(): matchInput.getTeam2();
    }
    match.setId(Long.getLong(matchInput.getId()));
    match.setCity(matchInput.getCity());
    match.setDate(LocalDate.parse(matchInput.getDate(),DateTimeFormatter.ofPattern("M/d/yyyy").withLocale(Locale.US)));
    match.setPlayerOfMatch(matchInput.getPlayer_of_match());
    match.setTeam1(firstTeam);
    match.setTeam2(secondTeam);
    match.setResult(matchInput.getResult());
    match.setResultMargin(match.getResultMargin());
    match.setMatchWinner(matchInput.getWinner());
    match.setTossDecision(match.getTossDecision());
    match.setTossWinner(match.getTossWinner());
    match.setVenue(match.getVenue());
    match.setUmpire1(matchInput.getUmpire1());
    match.setUmpire2(matchInput.getUmpire2());
    return match;
}
}