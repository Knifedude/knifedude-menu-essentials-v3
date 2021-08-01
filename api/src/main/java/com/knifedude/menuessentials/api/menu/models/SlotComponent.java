package com.knifedude.menuessentials.api.menu.models;

import com.knifedude.menuessentials.api.common.validation.Assert;
import com.knifedude.menuessentials.api.item.models.ItemStack;
import com.knifedude.menuessentials.api.item.models.ItemType;
import com.knifedude.menuessentials.api.text.models.Text;
import com.knifedude.menuessentials.api.text.models.lore.Lore;

import java.util.Objects;
import java.util.Optional;

public class SlotComponent extends MenuComponent {

    private ItemStack displayItem;
    private MenuSlot menuSlot;
    private boolean isHidden;

    public SlotComponent(ItemStack displayItem) {
        this.displayItem = displayItem.copy();
    }

    public SlotComponent(ItemType displayItemType, Text displayName) {
        Assert.notNull(displayItemType, "displayItemType");
        Assert.notNull(displayName, "displayName");

        this.displayItem = ItemStack.builder()
                .withDisplayName(displayName)
                .withItemType(displayItemType)
                .build();
    }

    public void hide() {
        if (!isHidden) {
            this.isHidden = true;
            slot().ifPresent(slot -> slot.setComponent(this));
        }
    }

    public void show() {
        if (isHidden) {
            this.isHidden = false;
            slot().ifPresent(slot -> slot.setComponent(this));
        }
    }

    public boolean isHidden() {
        return isHidden;
    }

    public boolean isVisible() {
        return !isHidden;
    }

    ItemStack getDisplayItem() {
        return displayItem.copy();
    }

    public Text getDisplayName() {
        return displayItem.getDisplayName().orElse(Text.empty());
    }

    public int getQuantity() {
        return displayItem.getQuantity();
    }

    public ItemType getDisplayItemType() {
        return displayItem.getItemType();
    }

    public Lore getLore() {
        Lore lore = displayItem.getLore();
        return lore.copy();
    }

    public void setDisplayItem(ItemStack itemStack) {
        Assert.notNull(itemStack, "itemStack");
        this.displayItem = itemStack;
        update();
    }

    public void setDisplayItemType(ItemType itemType) {
        Assert.notNull(itemType, "itemType");
        this.displayItem.setItemType(itemType);
        update();
    }

    public void setDisplayName(Text name) {
        Assert.notNull(name, "name");
        this.displayItem.setDisplayName(name);
        update();
    }

    public void setDisplayQuantity(int quantity) {
        Assert.greaterThanZero(quantity, "quantity");
        this.displayItem.setQuantity(quantity);
        update();
    }

    public void setDisplayLore(Lore lore) {
        this.displayItem.setLore(lore);
        update();
    }

    Optional<MenuSlot> slot() {
        return Optional.ofNullable(menuSlot);
    }

    private void update() {
        slot().ifPresent(s -> {
            s.setComponent(this);
        });
    }


    void detach() {
        if (menuSlot != null) {
            MenuSlot temp = menuSlot;
            this.menuSlot = null;
            temp.clear();
        }
    }

    void attach(MenuSlot menuSlot) {
        detach();
        if (this.menuSlot == null) {
            this.menuSlot = menuSlot;
            this.menuSlot.setComponent(this);
        }
    }

    public boolean matchesId(SlotComponent other) {
        if (other == null) return false;
        return Objects.equals(this.getUniqueId(), other.getUniqueId());
    }

}
