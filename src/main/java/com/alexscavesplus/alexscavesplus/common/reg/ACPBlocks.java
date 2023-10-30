package com.alexscavesplus.alexscavesplus.common.reg;

import com.alexscavesplus.alexscavesplus.AlexsCavesPlus;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.CavePaintingBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ACPBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, AlexsCavesPlus.MODID);
    public static final RegistryObject<Block> ANCIENT_AXOLOTL_PAINTING = regBlock("ancient_axolotl_painting", CavePaintingBlock::new);

    private static <T extends Block> RegistryObject<T> regBlock(String name, Supplier<T> block){
        RegistryObject<T> returns = BLOCKS.register(name, block);
        regBlockNItem(name, returns);
        return returns;
    }

    private static <T extends Block> RegistryObject<Item> regBlockNItem(String name, RegistryObject<T> block) {
        return ACPItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus event){
        BLOCKS.register(event);
    }

}
