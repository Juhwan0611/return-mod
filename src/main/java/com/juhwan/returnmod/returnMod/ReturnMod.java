package com.juhwan.returnmod.returnMod;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class ReturnMod implements ModInitializer {

    // 아이템 정의
    public static final Item RETURN_SCROLL = new ReturnScrollItem();

    @Override
    public void onInitialize() {
        // 아이템 등록
        Registry.register(Registries.ITEM, new Identifier("return-mod", "return_scroll"), RETURN_SCROLL);
        System.out.println("귀환서 아이템이 등록되었습니다!");
    }
}
