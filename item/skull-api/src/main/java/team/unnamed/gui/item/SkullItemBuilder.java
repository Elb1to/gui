package team.unnamed.gui.item;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import team.unnamed.bukkit.ServerVersion;
import team.unnamed.gui.item.skull.SkullSkin;

import java.lang.reflect.Field;
import java.util.UUID;

public class SkullItemBuilder
        extends ItemBuilderLayout<SkullItemBuilder> {

    private static final Field PROFILE_FIELD;
    private static final Material SKULL_MATERIAL;
    private static final byte DATA;

    static {
        try {
            Class<?> metaClass = Class.forName(
                    "org.bukkit.craftbukkit."
                            + ServerVersion.CURRENT +
                            ".inventory.CraftMetaSkull"
            );

            PROFILE_FIELD = metaClass.getDeclaredField("profile");

            SKULL_MATERIAL = Material.getMaterial("PLAYER_HEAD");
            DATA = 0;
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            throw new IllegalStateException("Cannot get the SkullMeta profile field!", e);
        }
    }

    private final SkullSkin skin;

    private SkullItemBuilder(int amount, SkullSkin skin) {
        super(SKULL_MATERIAL, amount, DATA);
        this.skin = skin;
    }

    public static SkullItemBuilder create(SkullSkin skin) {
        return new SkullItemBuilder(1, skin);
    }

    public static SkullItemBuilder create(int amount, SkullSkin skin) {
        return new SkullItemBuilder(amount, skin);
    }

    @Override
    public ItemStack build() {
        ItemStack itemStack = super.build();
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

        if (skin.getName() != null) {
            skullMeta.setOwner(skin.getName());
        } else {
            UUID uuid = UUID.randomUUID();
            PlayerProfile playerProfile = Bukkit.getServer().createProfile(uuid, uuid.toString());

            if (skin.getSignature() != null) {
                playerProfile.setProperty(new ProfileProperty(
                        "textures",
                        skin.getValue(),
                        skin.getSignature()
                ));
            } else {
                playerProfile.setProperty(new ProfileProperty(
                        "textures",
                        skin.getValue())
                );
            }

            skullMeta.setPlayerProfile(playerProfile);
        }


        itemStack.setItemMeta(skullMeta);

        return itemStack;
    }

    @Override
    protected SkullItemBuilder back() {
        return this;
    }
}
