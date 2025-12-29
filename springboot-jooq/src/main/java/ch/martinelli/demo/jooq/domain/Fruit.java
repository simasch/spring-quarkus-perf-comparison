package ch.martinelli.demo.jooq.domain;

import java.util.List;

public record Fruit(Long id, String name, String description, List<StoreFruitPrice> storePrices) {
}
