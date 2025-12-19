package com.jett.flowershop.domain.repository;

import com.jett.flowershop.domain.entity.ShopInfo;

import java.util.Optional;

public interface ShopInfoRepository {
    ShopInfo save(ShopInfo shopInfo);
    Optional<ShopInfo> findFirst();
    boolean exists();
}
