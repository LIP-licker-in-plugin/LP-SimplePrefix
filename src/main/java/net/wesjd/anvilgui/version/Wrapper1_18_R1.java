package net.wesjd.anvilgui.version;

import net.minecraft.core.BlockPosition;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.protocol.game.PacketPlayOutCloseWindow;
import net.minecraft.network.protocol.game.PacketPlayOutOpenWindow;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.IInventory;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.inventory.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R1.event.CraftEventFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import net.wesjd.anvilgui.version.special.AnvilContainer1_18_R1;

public class Wrapper1_18_R1 implements VersionWrapper {

    private final boolean IS_ONE_EIGHTEEN = Bukkit.getBukkitVersion().contains("1.18");

    private int getRealNextContainerId(Player player) {
        return toNMS(player).nextContainerCounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNextContainerId(Player player, Object container) {
        if (IS_ONE_EIGHTEEN){
            return ((AnvilContainer1_18_R1) container).getContainerId();
        }
        return ((AnvilContainer) container).getContainerId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleInventoryCloseEvent(Player player) {
        CraftEventFactory.handleInventoryCloseEvent(toNMS(player));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendPacketOpenWindow(Player player, int containerId, String guiTitle) {
        toNMS(player).b.a(new PacketPlayOutOpenWindow(containerId, Containers.h, new ChatComponentText(guiTitle)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendPacketCloseWindow(Player player, int containerId) {
        toNMS(player).b.a(new PacketPlayOutCloseWindow(containerId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setActiveContainerDefault(Player player) {
        (toNMS(player)).bW = (toNMS(player)).bW;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setActiveContainer(Player player, Object container) {
        (toNMS(player)).bW = (Container)container;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setActiveContainerId(Object container, int containerId) {
        //noop
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addActiveContainerSlotListener(Object container, Player player) {
        toNMS(player).a((Container) container);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Inventory toBukkitInventory(Object container) {
        return ((Container) container).getBukkitView().getTopInventory();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object newContainerAnvil(Player player, String guiTitle) {
        if (IS_ONE_EIGHTEEN){
            return new AnvilContainer1_18_R1(player,getRealNextContainerId(player),guiTitle);
        }
        return new AnvilContainer(player, guiTitle);
    }

    /**
     * Turns a {@link Player} into an NMS one
     *
     * @param player The player to be converted
     * @return the NMS EntityPlayer
     */
    private EntityPlayer toNMS(Player player) {
        return ((CraftPlayer) player).getHandle();
    }

    /**
     * Modifications to ContainerAnvil that makes it so you don't have to have xp to use this anvil
     */
    private class AnvilContainer extends ContainerAnvil {
        public AnvilContainer(Player player, String guiTitle) {
            super(Wrapper1_18_R1.this.getRealNextContainerId(player), ((CraftPlayer)player).getHandle().fq(),
                    ContainerAccess.a(((CraftWorld)player.getWorld()).getHandle(), new BlockPosition(0, 0, 0)));
            this.checkReachable = false;
            setTitle(new ChatMessage(guiTitle));
        }

        @Override
        public void i() {
            super.i();
            this.w.a(0);
        }

        @Override
        public void b(EntityHuman player) {}

        @Override
        protected void a(EntityHuman player, IInventory container) {}

        public int getContainerId() {
            return this.j;
        }
    }
}
