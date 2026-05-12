package me.mohapre;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import fr.xephi.authme.api.v3.AuthMeApi;

public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // تفعيل المستمع للأحداث (Events) لكي يعرف البلوجن متى يدخل اللاعب
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("§a[BedrockAutoLogin] تم تفعيل البلوجن بنجاح!");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

        // التحقق إذا كان اسم اللاعب يبدأ بنقطة (لاعب بدروك)
        if (playerName.startsWith(".")) {
            // تأخير الكود لثانية واحدة (20 tick) لضمان استقرار اتصال اللاعب
            getServer().getScheduler().runTaskLater(this, () -> {
                if (player.isOnline()) {
                    AuthMeApi api = AuthMeApi.getInstance();
                    
                    if (api.isRegistered(playerName)) {
                        // إذا كان مسجلاً مسبقاً، سجل دخوله فوراً
                        api.forceLogin(player);
                        player.sendMessage("§a[AutoLogin] تم التعرف عليك، سجلنا دخولك تلقائياً!");
                    } else {
                        // إذا كان لاعباً جديداً، سجله بباسورد افتراضي وسجل دخوله
                        api.forceRegister(player, "Bedrock_Auto_Pass", true);
                        player.sendMessage("§a[AutoLogin] أهلاً بك! تم إنشاء حسابك وتأمين دخولك.");
                    }
                }
            }, 20L);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("§c[BedrockAutoLogin] تم إيقاف البلوجن.");
    }
                          }

