package com.jett.flowershop.infrastructure.repository;

import com.jett.flowershop.domain.entity.Banner;
import com.jett.flowershop.domain.repository.BannerRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class BannerRepositoryImpl implements BannerRepository {

    private final ConcurrentHashMap<Long, Banner> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Banner save(Banner banner) {
        if (banner.getId() == null) {
            banner.setId(idGenerator.getAndIncrement());
        }
        storage.put(banner.getId(), banner);
        return banner;
    }

    @Override
    public Optional<Banner> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Banner> findAll() {
        return storage.values().stream()
                .sorted(Comparator.comparing(Banner::getDisplayOrder))
                .collect(Collectors.toList());
    }

    @Override
    public List<Banner> findActiveBanners() {
        LocalDateTime now = LocalDateTime.now();
        return storage.values().stream()
                .filter(banner -> banner.getIsActive() != null && banner.getIsActive())
                .filter(banner -> {
                    boolean afterStart = banner.getStartDate() == null || !now.isBefore(banner.getStartDate());
                    boolean beforeEnd = banner.getEndDate() == null || !now.isAfter(banner.getEndDate());
                    return afterStart && beforeEnd;
                })
                .sorted(Comparator.comparing(Banner::getDisplayOrder))
                .collect(Collectors.toList());
    }

    @Override
    public List<Banner> findByType(String bannerType) {
        if (bannerType == null) return new ArrayList<>();
        return storage.values().stream()
                .filter(banner -> bannerType.equals(banner.getBannerType()))
                .sorted(Comparator.comparing(Banner::getDisplayOrder))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }

    @Override
    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }
}
