package ch.martinelli.demo.jooq.repository;

import ch.martinelli.demo.jooq.domain.Address;
import ch.martinelli.demo.jooq.domain.Fruit;
import ch.martinelli.demo.jooq.domain.Store;
import ch.martinelli.demo.jooq.domain.StoreFruitPrice;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static ch.martinelli.demo.jooq.db.Sequences.FRUITS_SEQ;
import static ch.martinelli.demo.jooq.db.Tables.*;
import static org.jooq.Records.mapping;
import static org.jooq.impl.DSL.multiset;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;

@Repository
public class FruitRepository {

    private final DSLContext dsl;

    public FruitRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public List<Fruit> findAll() {
        return dsl.select(
                        FRUITS.ID,
                        FRUITS.NAME,
                        FRUITS.DESCRIPTION,
                        multiset(
                                select(
                                        row(
                                                STORES.ID,
                                                STORES.NAME,
                                                row(STORES.ADDRESS, STORES.CITY, STORES.COUNTRY).mapping(Address::new),
                                                STORES.CURRENCY
                                        ).mapping(Store::new),
                                        STORE_FRUIT_PRICES.PRICE
                                )
                                        .from(STORE_FRUIT_PRICES)
                                        .join(STORES).on(STORES.ID.eq(STORE_FRUIT_PRICES.STORE_ID))
                                        .where(STORE_FRUIT_PRICES.FRUIT_ID.eq(FRUITS.ID))
                        ).convertFrom(r -> r.map(mapping(StoreFruitPrice::new)))
                )
                .from(FRUITS)
                .fetch(mapping(Fruit::new));
    }

    public Optional<Fruit> findByName(String name) {
        return dsl.select(
                        FRUITS.ID,
                        FRUITS.NAME,
                        FRUITS.DESCRIPTION,
                        multiset(
                                select(
                                        row(
                                                STORES.ID,
                                                STORES.NAME,
                                                row(STORES.ADDRESS, STORES.CITY, STORES.COUNTRY).mapping(Address::new),
                                                STORES.CURRENCY
                                        ).mapping(Store::new),
                                        STORE_FRUIT_PRICES.PRICE
                                )
                                        .from(STORE_FRUIT_PRICES)
                                        .join(STORES).on(STORES.ID.eq(STORE_FRUIT_PRICES.STORE_ID))
                                        .where(STORE_FRUIT_PRICES.FRUIT_ID.eq(FRUITS.ID))
                        ).convertFrom(r -> r.map(mapping(StoreFruitPrice::new)))
                )
                .from(FRUITS)
                .where(FRUITS.NAME.eq(name))
                .fetchOptional(mapping(Fruit::new));
    }

    public Fruit save(Fruit fruit) {
        Long id = dsl.nextval(FRUITS_SEQ);
        dsl.insertInto(FRUITS)
                .set(FRUITS.ID, id)
                .set(FRUITS.NAME, fruit.name())
                .set(FRUITS.DESCRIPTION, fruit.description())
                .execute();

        if (fruit.storePrices() != null && !fruit.storePrices().isEmpty()) {
            var insert = dsl.insertInto(STORE_FRUIT_PRICES,
                    STORE_FRUIT_PRICES.FRUIT_ID,
                    STORE_FRUIT_PRICES.STORE_ID,
                    STORE_FRUIT_PRICES.PRICE);
            for (StoreFruitPrice sfp : fruit.storePrices()) {
                insert = insert.values(id, sfp.store().id(), sfp.price());
            }
            insert.execute();
        }

        return new Fruit(id, fruit.name(), fruit.description(), fruit.storePrices());
    }
}
