package org.acme.repository;

import java.util.List;
import java.util.Optional;

import org.acme.domain.Fruit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FruitRepository extends JpaRepository<Fruit, Long> {


	@Query("select f from Fruit f join fetch f.storePrices sp join fetch sp.store s where f.name = :name")
	Optional<Fruit> findByName(String name);

	@Query("select f from Fruit f join fetch f.storePrices sp join fetch sp.store s")
	List<Fruit> findAll();
}
