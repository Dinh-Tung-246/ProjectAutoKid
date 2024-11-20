package com.example.demo.repository;

import com.example.demo.model.HoaDonHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HoaDonHistoryRepo extends JpaRepository<HoaDonHistory,Integer> {
}
