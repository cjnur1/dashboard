package com.example.dashboard;

import com.example.dashboard.model.MatchOutput;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutPutRepo extends JpaRepository<MatchOutput,Long> {
}
