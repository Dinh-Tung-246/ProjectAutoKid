package com.example.demo.repository;

import com.example.demo.model.MauSac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MauSacRepo extends JpaRepository<MauSac,Integer> {
    boolean existsByMaMS(String maMS);
}
