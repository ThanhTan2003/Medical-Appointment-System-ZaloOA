package com.programmingtechie.zalo_oa_service.repository.token;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.zalo_oa_service.entity.Token;

public interface TokenRepository extends JpaRepository<Token, String> {}
