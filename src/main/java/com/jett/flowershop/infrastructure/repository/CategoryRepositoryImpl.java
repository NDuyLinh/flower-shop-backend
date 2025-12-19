package com.jett.flowershop.infrastructure.repository;

import com.jett.flowershop.domain.entity.Category;
import com.jett.flowershop.domain.repository.CategoryRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    private final ConcurrentHashMap<Long, Category> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Category save(Category category) {
        if (category.getId() == null) {
            category.setId(idGenerator.getAndIncrement());
        }
        storage.put(category.getId(), category);
        return category;
    }

    @Override
    public Optional<Category> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Category> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }

    @Override
    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }

    @Override
    public List<Category> findFeaturedCategories() {
        return storage.values().stream()
                .filter(Category::isFeatured)
                .filter(category -> "ACTIVE".equals(category.getStatus()))
                .sorted(java.util.Comparator.comparing(Category::getDisplayOrder))
                .collect(java.util.stream.Collectors.toList());
    }
}
