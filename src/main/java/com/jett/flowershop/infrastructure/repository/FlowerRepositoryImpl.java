package com.jett.flowershop.infrastructure.repository;

import com.jett.flowershop.domain.entity.Flower;
import com.jett.flowershop.domain.repository.FlowerRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class FlowerRepositoryImpl implements FlowerRepository {

    private final ConcurrentHashMap<Long, Flower> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Flower save(Flower flower) {
        if (flower.getId() == null) {
            flower.setId(idGenerator.getAndIncrement());
        }
        storage.put(flower.getId(), flower);
        return flower;
    }

    @Override
    public Optional<Flower> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Flower> findAll() {
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
    public List<Flower> searchByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return new ArrayList<>();
        }
        String searchTerm = name.toLowerCase().trim();
        return storage.values().stream()
                .filter(flower -> flower.getName() != null && 
                        flower.getName().toLowerCase().contains(searchTerm))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<Flower> findByOccasion(String occasion) {
        if (occasion == null || occasion.trim().isEmpty()) {
            return new ArrayList<>();
        }
        String searchOccasion = occasion.toLowerCase().trim();
        return storage.values().stream()
                .filter(flower -> flower.getOccasion() != null && 
                        flower.getOccasion().toLowerCase().equals(searchOccasion))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<Flower> findByColor(String color) {
        if (color == null || color.trim().isEmpty()) {
            return new ArrayList<>();
        }
        String searchColor = color.toLowerCase().trim();
        return storage.values().stream()
                .filter(flower -> flower.getColor() != null && 
                        flower.getColor().toLowerCase().equals(searchColor))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<Flower> findByPriceRange(java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice) {
        return storage.values().stream()
                .filter(flower -> {
                    if (flower.getPrice() == null) return false;
                    boolean minCondition = minPrice == null || flower.getPrice().compareTo(minPrice) >= 0;
                    boolean maxCondition = maxPrice == null || flower.getPrice().compareTo(maxPrice) <= 0;
                    return minCondition && maxCondition;
                })
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<Flower> searchByNameOrOccasion(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }
        String searchTerm = keyword.toLowerCase().trim();
        return storage.values().stream()
                .filter(flower -> 
                        (flower.getName() != null && flower.getName().toLowerCase().contains(searchTerm)) ||
                        (flower.getOccasion() != null && flower.getOccasion().toLowerCase().contains(searchTerm)))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<Flower> findHotFlowers() {
        return storage.values().stream()
                .filter(Flower::isHot)
                .filter(flower -> "ACTIVE".equals(flower.getStatus()))
                .sorted(java.util.Comparator.comparing(Flower::getViewCount).reversed())
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<Flower> findFeaturedFlowers() {
        return storage.values().stream()
                .filter(Flower::isFeatured)
                .filter(flower -> "ACTIVE".equals(flower.getStatus()))
                .sorted(java.util.Comparator.comparing(Flower::getViewCount).reversed())
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<Flower> findBestSelling(int limit) {
        return storage.values().stream()
                .filter(flower -> "ACTIVE".equals(flower.getStatus()))
                .sorted(java.util.Comparator.comparing(Flower::getSoldCount).reversed())
                .limit(limit)
                .collect(java.util.stream.Collectors.toList());
    }
}
