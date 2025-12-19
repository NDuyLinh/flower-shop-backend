package com.jett.flowershop.application.service;

import com.jett.flowershop.domain.entity.Banner;
import com.jett.flowershop.domain.repository.BannerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Application service handling Banner use cases.
 */
@Service
public class BannerService {

    private final BannerRepository bannerRepository;

    public BannerService(BannerRepository bannerRepository) {
        this.bannerRepository = bannerRepository;
    }

    public Banner createBanner(Banner banner) {
        if (banner.getCreatedAt() == null) {
            banner.setCreatedAt(LocalDateTime.now());
        }
        return bannerRepository.save(banner);
    }

    public List<Banner> getAllBanners() {
        return bannerRepository.findAll();
    }

    public List<Banner> getActiveBanners() {
        return bannerRepository.findActiveBanners();
    }

    public List<Banner> getBannersByType(String bannerType) {
        return bannerRepository.findByType(bannerType);
    }

    public Optional<Banner> getBannerById(Long id) {
        return bannerRepository.findById(id);
    }

    public Banner updateBanner(Long id, Banner banner) {
        Optional<Banner> existing = bannerRepository.findById(id);
        if (existing.isEmpty()) {
            throw new RuntimeException("Banner not found with id: " + id);
        }
        
        Banner existingBanner = existing.get();
        existingBanner.setTitle(banner.getTitle());
        existingBanner.setSubtitle(banner.getSubtitle());
        existingBanner.setDescription(banner.getDescription());
        existingBanner.setImageUrl(banner.getImageUrl());
        existingBanner.setLinkUrl(banner.getLinkUrl());
        existingBanner.setBannerType(banner.getBannerType());
        existingBanner.setDisplayOrder(banner.getDisplayOrder());
        existingBanner.setIsActive(banner.getIsActive());
        existingBanner.setStartDate(banner.getStartDate());
        existingBanner.setEndDate(banner.getEndDate());
        
        return bannerRepository.save(existingBanner);
    }

    public void deleteBanner(Long id) {
        if (!bannerRepository.existsById(id)) {
            throw new RuntimeException("Banner not found with id: " + id);
        }
        bannerRepository.deleteById(id);
    }
}
