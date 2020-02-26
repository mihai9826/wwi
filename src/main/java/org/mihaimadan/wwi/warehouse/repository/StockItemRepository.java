package org.mihaimadan.wwi.warehouse.repository;

import org.mihaimadan.wwi.warehouse.model.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockItemRepository extends JpaRepository<StockItem, Long> {
}
