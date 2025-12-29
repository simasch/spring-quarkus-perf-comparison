package ch.martinelli.demo.jooq.domain;

public record Address(
        String address,
        String city,
        String country
) {
}
