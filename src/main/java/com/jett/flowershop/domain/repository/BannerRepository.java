package com.jett.flowershop.domain.repository;

import com.jett.flowershop.domain.entity.Banner;

import java.util.List;
import java.util.Optional;

/**
 * Domain repository interface for Banner entity.
 */
public interface BannerRepository {
    
    Banner save(Banner banner);
    
    Optional<Banner> findById(Long id);
    
    List<Banner> findAll();
    
    List<Banner> findActiveBanners();
    
    List<Banner> findByType(String bannerType);
    
    void deleteById(Long id);
    
    boolean existsById(Long id);
}
