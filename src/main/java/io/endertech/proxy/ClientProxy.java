package io.endertech.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.endertech.EnderTech;
import io.endertech.block.ETBlocks;
import io.endertech.client.handler.DrawBlockHighlightEventHandler;
import io.endertech.client.handler.KeyBindingHandler;
import io.endertech.modules.dev.renderer.SpinningCubeRenderer;
import io.endertech.modules.dev.fluid.DevETFluids;
import io.endertech.multiblock.handler.MultiblockClientTickHandler;
import io.endertech.multiblock.renderer.ConnectedTextureRenderer;
import io.endertech.multiblock.renderer.TankControllerRenderer;
import io.endertech.multiblock.tile.TileTankController;
import io.endertech.tile.TileSpinningCube;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy
{
    @Override
    public void registerTickerHandlers()
    {
        super.registerTickerHandlers();

        FMLCommonHandler.instance().bus().register(new KeyBindingHandler());
        FMLCommonHandler.instance().bus().register(new MultiblockClientTickHandler());
        MinecraftForge.EVENT_BUS.register(new DrawBlockHighlightEventHandler());
    }

    @Override
    public void registerTESRs()
    {
        if (EnderTech.loadDevModeContent)
        {
            ClientRegistry.bindTileEntitySpecialRenderer(TileSpinningCube.class, new SpinningCubeRenderer());
        }

        ClientRegistry.bindTileEntitySpecialRenderer(TileTankController.class, new TankControllerRenderer());
    }

    @Override
    public void registerItemRenderers()
    {
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ETBlocks.blockTankController), new TankControllerRenderer());
    }

    @Override
    public void registerRenderers()
    {
        connectedTexturesRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new ConnectedTextureRenderer());
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void registerIcons(TextureStitchEvent.Pre event)
    {
        DevETFluids.fluidChargedEnderStill = event.map.registerIcon("endertech:fluids/charged_ender_still");
        DevETFluids.fluidChargedEnderFlowing = event.map.registerIcon("endertech:fluids/charged_ender_flow");
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void initializeIcons(TextureStitchEvent.Post event)
    {
        if (EnderTech.loadDevModeContent)
        {
            DevETFluids.fluidChargedEnder.setIcons(DevETFluids.fluidChargedEnderStill, DevETFluids.fluidChargedEnderFlowing);
        }
    }
}
