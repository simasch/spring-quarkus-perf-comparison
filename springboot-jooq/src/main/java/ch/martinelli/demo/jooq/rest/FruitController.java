package ch.martinelli.demo.jooq.rest;

import ch.martinelli.demo.jooq.domain.Fruit;
import ch.martinelli.demo.jooq.repository.FruitRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fruits")
public class FruitController {
	private final FruitRepository fruitRepository;

	public FruitController(FruitRepository fruitRepository) {
		this.fruitRepository = fruitRepository;
	}

	@GetMapping
	public List<Fruit> getAll() {
		return this.fruitRepository.findAll();
	}

	@GetMapping(path = "/{name}")
	public ResponseEntity<Fruit> getFruit(@PathVariable String name) {
		return this.fruitRepository.findByName(name)
			.map(ResponseEntity::ok)
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@Transactional
	@PostMapping
	public Fruit addFruit(@RequestBody Fruit fruit) {
		return this.fruitRepository.save(fruit);
	}
}
