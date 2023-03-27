package com.somemone.dynamiceeconomy.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class APIPresence {

    @Getter
    private final boolean quickShop;

    @Getter
    private final boolean chestShop;

    @Getter
    private final boolean shopChest;

    @Getter
    private final boolean signShop;

}
