package net.auscraft.BlivUtils;

import java.util.HashMap;
import java.util.logging.Logger;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public final class BlivUtils extends JavaPlugin
{
    Logger log;
    public static BlivUtils plugin;
    private static HashMap<String, Integer> promoteCount = new HashMap<String, Integer>();

    public void onEnable()
    {
        getCommand("rank").setExecutor(new RankHelpExecuter());
        getCommand("say").setExecutor(new GenericExecuter());
        getCommand("wstop").setExecutor(new GenericExecuter());
        getCommand("sudo").setExecutor(new GenericExecuter());
        getCommand("buyrank").setExecutor(new PromoteExecuter(this));
        getCommand("promoteme").setExecutor(new PromoteExecuter(this));
        log = getLogger();
    }

    public void onDisable()
    {
    }

    public Permission setupPermissions()
    {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        Permission perms = (Permission)rsp.getProvider();
        return perms;
    }

    public Economy setupEconomy()
    {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if(rsp != null);
        Economy econ = (Economy)rsp.getProvider();
        return econ;
    }

    public static HashMap<String, Integer> getPromote()
    {
        return promoteCount;
    }

    public static BlivUtils getPlugin()
    {
        return plugin;
    }

}
