package com.haircutAPI.HaircutAPI.repositories.Authentication;

import org.springframework.data.jpa.repository.JpaRepository;

import com.haircutAPI.HaircutAPI.enity.Worker;
import java.util.Optional;


public interface WorkerAuthRepository extends JpaRepository<Worker,String> {

    boolean existsByUsername(String username);

    Optional<Worker> findByUsername(String username);

}
