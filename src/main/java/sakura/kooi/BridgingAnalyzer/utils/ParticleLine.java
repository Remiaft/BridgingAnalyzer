package sakura.kooi.BridgingAnalyzer.utils;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class ParticleLine {
    public static void drawParticleLine(Location var0, Location var1, ParticleEffects var2) {
        drawParticleLine(var0, var1, var2, (int)(var0.distance(var1) * 8.0D));
    }

    public static void drawParticleLine(Location var0, Location var1, ParticleEffects var2, int var3) {
        Location var4 = var0.clone();
        Location var5 = var1.clone();
        double var6 = var0.distanceSquared(var1) * var3;
        Vector var8 = var5.toVector().subtract(var4.toVector());
        float var9 = (float)var8.length();
        var8.normalize();
        float var10 = var9 / (float)var6;
        Vector var11 = var8.multiply(var10);
        Location var12 = var4.clone().subtract(var11);
        int var13 = 0;
        for (int var14 = 0; var14 < var6; var14++) {
            if (var13 >= var6)
                var13 = 0;
            var13++;
            var12.add(var11);
            var2.display(0.0F, 0.0F, 0.0F, 0.0F, 1, var12, 50.0D);
        }
    }
}
