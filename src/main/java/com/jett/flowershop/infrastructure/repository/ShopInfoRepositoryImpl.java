package com.jett.flowershop.infrastructure.repository;

import com.jett.flowershop.domain.entity.ShopInfo;
import com.jett.flowershop.domain.repository.ShopInfoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Repository
public class ShopInfoRepositoryImpl implements ShopInfoRepository {
    
    private final AtomicReference<ShopInfo> storage = new AtomicReference<>();

    @Override
    public ShopInfo save(ShopInfo shopInfo) {
        storage.set(shopInfo);
        return shopInfo;
    }

    @Override
    public Optional<ShopInfo> findFirst() {
        return Optional.ofNullable(storage.get());
    }

    @Override
    public boolean exists() {
        return storage.get() != null;
    }
}
