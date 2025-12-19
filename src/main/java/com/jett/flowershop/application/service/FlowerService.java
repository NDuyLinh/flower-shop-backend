package com.jett.flowershop.application.service;

import com.jett.flowershop.domain.entity.Flower;
import com.jett.flowershop.domain.repository.FlowerRepository;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

/**
 * Application service handling Flower use cases.
 *
 * Use cases:
 * - Create new Flower
 * - Get all Flowers
 * - Get Flower by ID
 * - Update Flower
 * - Delete Flower
 *
 * Rules:
 * - Depends on domain only
 * - No Spring annotations
 */
@Service
public class FlowerService {

    private final FlowerRepository flowerRepository;

    public FlowerService(FlowerRepository flowerRepository) {
        this.flowerRepository = flowerRepository;
    }

    public Flower createFlower(Flower flower) {
        if (flower.getCreatedAt() == null) {
            flower.setCreatedAt(LocalDateTime.now());
        }
        return flowerRepository.save(flower);
    }

    public java.util.List<Flower> getAllFlowers() {
        return flowerRepository.findAll();
    }

    public java.util.Optional<Flower> getFlowerById(Long id) {
        return flowerRepository.findById(id);
    }

    public Flower updateFlower(Long id, Flower flower) {
        java.util.Optional<Flower> existing = flowerRepository.findById(id);
        if (existing.isEmpty()) {
            throw new RuntimeException("Flower not found with id: " + id);
        }
        
        Flower existingFlower = existing.get();
        existingFlower.setName(flower.getName());
        existingFlower.setPrice(flower.getPrice());
        existingFlower.setDescription(flower.getDescription());
        
        return flowerRepository.save(existingFlower);
    }

    public void deleteFlower(Long id) {
        if (!flowerRepository.existsById(id)) {
            throw new RuntimeException("Flower not found with id: " + id);
        }
        flowerRepository.deleteById(id);
    }

    public java.util.List<Flower> searchFlowersByName(String name) {
        return flowerRepository.searchByName(name);
    }

    public java.util.List<Flower> filterFlowersByOccasion(String occasion) {
        return flowerRepository.findByOccasion(occasion);
    }

    public java.util.List<Flower> filterFlowersByColor(String color) {
        return flowerRepository.findByColor(color);
    }

    public java.util.List<Flower> filterFlowersByPriceRange(java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice) {
        return flowerRepository.findByPriceRange(minPrice, maxPrice);
    }

    public java.util.List<Flower> searchFlowersByNameOrOccasion(String keyword) {
        return flowerRepository.searchByNameOrOccasion(keyword);
    }

    // Homepage methods
    public java.util.List<Flower> getHotFlowers() {
        return flowerRepository.findHotFlowers();
    }

    public java.util.List<Flower> getFeaturedFlowers() {
        return flowerRepository.findFeaturedFlowers();
    }

    public java.util.List<Flower> getBestSellingFlowers(int limit) {
        return flowerRepository.findBestSelling(limit);
    }
}
