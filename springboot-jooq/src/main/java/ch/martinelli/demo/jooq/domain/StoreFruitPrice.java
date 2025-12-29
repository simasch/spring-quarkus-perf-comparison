package ch.martinelli.demo.jooq.domain;

import java.math.BigDecimal;

public record StoreFruitPrice(Store store, BigDecimal price) {
}
