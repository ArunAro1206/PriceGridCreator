package com.assignment.price_grid.repository;

import com.assignment.price_grid.model.PriceCell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceGridRepository extends JpaRepository<PriceCell, Long> {
    List<PriceCell> findAllByOrderByHeightAscWidthAsc();
}
