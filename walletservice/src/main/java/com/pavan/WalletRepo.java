package com.pavan;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepo extends JpaRepository<Wallet, Integer> {
    Wallet findByUserId(Integer userId);

    @Modifying
    @Transactional
    @Query("update Wallet w set w.balance = w.balance + ?2 where w.userId = ?1")
    void updateWallet(Integer userId, Double amount);
}

