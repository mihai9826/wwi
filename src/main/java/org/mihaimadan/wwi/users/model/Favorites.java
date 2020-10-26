package org.mihaimadan.wwi.users.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mihaimadan.wwi.warehouse.model.StockItem;

import javax.persistence.*;

@Entity
@Table(name = "Favorites", schema = "Application")
@Data
public class Favorites {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PersonID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "StockItemID")
    private StockItem stockItem;
}
