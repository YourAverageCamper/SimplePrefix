package com.zeusallmighty11.SimplePrefix;


import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/** Why are you viewing this? */
public class SimplePrefix extends JavaPlugin
{
    List<Prefix> prefixes;



    public void onEnable()
    {
        prefixes = new ArrayList<>();

        File dir = new File(getDataFolder() + "");
        if (!dir.exists())
            dir.mkdir();

        File config = new File(getDataFolder() + "/config.yml");
        if (!config.exists())
            saveDefaultConfig();

        ConfigurationSection cs = getConfig().getConfigurationSection("prefixes");
        for (String key : cs.getKeys(false))
        {
            String permission = cs.getString(key + ".permission");
            String prefix = cs.getString(key + ".prefix").replace("&", "§");
            String vip = cs.getString(key + ".vip");
            prefixes.add(new Prefix(prefix, permission, vip));
        }

        getServer().getPluginManager().registerEvents(new EVT_Chat(), this);
    }



    class Prefix
    {
        String prefix;
        String permission;
        String vip;



        public Prefix(String prefix, String permission, String vip)
        {
            this.prefix = prefix;
            this.permission = permission;
            this.vip = vip;
        }



        public String getPrefix()
        {
            return prefix;
        }



        public String getPermission()
        {
            return permission;
        }



        public String getVip()
        {
            return vip;
        }
    }



    class EVT_Chat implements Listener
    {
        @EventHandler
        public void onChat(PlayerChatEvent e)
        {
            Player p = e.getPlayer();
            String s = "";
            for (Prefix prefix : prefixes)
            {
                if (!p.hasPermission(prefix.getPermission()))
                    continue;

                if (prefix.getVip().equals(""))
                    s = prefix.getPrefix();
                else if (prefix.getVip().equals(p.getName()))
                    s = prefix.getPrefix();
            }
            e.setFormat(s + e.getPlayer().getName() + "§f: " + e.getMessage());
        }
    }


}
