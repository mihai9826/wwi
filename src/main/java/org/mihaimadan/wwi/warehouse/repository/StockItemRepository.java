package org.mihaimadan.wwi.warehouse.repository;

import org.mihaimadan.wwi.warehouse.model.StockItem;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockItemRepository extends JpaRepository<StockItem, Long> {
    List<StockItem> findFirst20ByTaxRate(double taxRate);
}
