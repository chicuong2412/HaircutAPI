package com.haircutAPI.HaircutAPI.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.haircutAPI.HaircutAPI.enity.FirebaseUserTokens;

public interface FirebaseTokenUsersRepository extends JpaRepository<FirebaseUserTokens, String> {



}
