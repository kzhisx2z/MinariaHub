package fr.kx2zh.minaria.hub.utils;

import fr.kx2zh.minaria.api.MinariaAPI;
import fr.kx2zh.minaria.api.permissions.IPermissionsEntity;

import java.util.UUID;

public class RankUtils {

    public static String getFormattedRank(UUID uuid) {
        final IPermissionsEntity permissionsEntity = MinariaAPI.get().getPermissionManager().getPlayer(uuid);

        final String prefix = permissionsEntity.getPrefix();
        final String tag = permissionsEntity.getTag();

        return tag + prefix;
    }

}
