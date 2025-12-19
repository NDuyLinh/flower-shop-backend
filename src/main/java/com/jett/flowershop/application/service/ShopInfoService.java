package com.jett.flowershop.application.service;

import com.jett.flowershop.domain.entity.ShopInfo;
import com.jett.flowershop.domain.repository.ShopInfoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ShopInfoService {
    
    private final ShopInfoRepository shopInfoRepository;

    public ShopInfoService(ShopInfoRepository shopInfoRepository) {
        this.shopInfoRepository = shopInfoRepository;
    }

    public ShopInfo getShopInfo() {
        return shopInfoRepository.findFirst()
                .orElseGet(this::createDefaultShopInfo);
    }

    public ShopInfo updateShopInfo(String shopName, String logoUrl, String aboutUs, 
                                   String description, String phone, String email, 
                                   String address, String workingHours, 
                                   String facebookUrl, String instagramUrl) {
        ShopInfo shopInfo = shopInfoRepository.findFirst()
                .orElseGet(ShopInfo::new);

        if (shopInfo.getId() == null) {
            shopInfo.setId(1L); // Singleton, always ID 1
        }
        shopInfo.setShopName(shopName);
        shopInfo.setLogoUrl(logoUrl);
        shopInfo.setAboutUs(aboutUs);
        shopInfo.setDescription(description);
        shopInfo.setPhone(phone);
        shopInfo.setEmail(email);
        shopInfo.setAddress(address);
        shopInfo.setWorkingHours(workingHours);
        shopInfo.setFacebookUrl(facebookUrl);
        shopInfo.setInstagramUrl(instagramUrl);
        shopInfo.setUpdatedAt(LocalDateTime.now());

        return shopInfoRepository.save(shopInfo);
    }

    private ShopInfo createDefaultShopInfo() {
        ShopInfo defaultInfo = new ShopInfo();
        defaultInfo.setId(1L);
        defaultInfo.setShopName("Flower Shop");
        defaultInfo.setLogoUrl("");
        defaultInfo.setAboutUs("Welcome to our flower shop");
        defaultInfo.setDescription("We provide the best flowers for all occasions");
        defaultInfo.setPhone("");
        defaultInfo.setEmail("");
        defaultInfo.setAddress("");
        defaultInfo.setWorkingHours("Mon-Sat: 8:00-20:00, Sun: 9:00-18:00");
        defaultInfo.setFacebookUrl("");
        defaultInfo.setInstagramUrl("");
        defaultInfo.setUpdatedAt(LocalDateTime.now());
        return shopInfoRepository.save(defaultInfo);
    }
}
