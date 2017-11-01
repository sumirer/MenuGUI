package sole.memory.guishop;

import cn.nukkit.Server;
import cn.nukkit.permission.Permission;
import cn.nukkit.plugin.PluginBase;
import sole.memory.guishop.command.AdminAddItemCommand;
import sole.memory.guishop.command.PlayerOpenShopCommand;
import sole.memory.guishop.database.ConfigDataBase;
import sole.memory.guishop.listener.EventListener;

/**
 *
 * on 2017/10/31.
 * @author SoleMemory
 */
public class GUIShop extends PluginBase{

    private static GUIShop plugin = null;

    public static GUIShop getInstance(){
        return GUIShop.plugin;
    }

    @Override
    public void onEnable() {
        GUIShop.plugin = this;
        this.getDataFolder().mkdirs();
        this.getLogger().info("this just test gui");
        Server.getInstance().getPluginManager().registerEvents(new EventListener(),this);
        ConfigDataBase.initData();
        registerPermission();
        registerCommand();
        this.getLogger().info("this plugin is load.....");
    }

    private void registerPermission(){
        Server.getInstance().getPluginManager().addPermission(new Permission("sole.memory.gui.admin","op"));
        Server.getInstance().getPluginManager().addPermission(new Permission("sole.memory.gui.player","true"));
    }


    private void registerCommand(){
        Server.getInstance().getCommandMap().register("GUIShop",new AdminAddItemCommand("gshop"));
        Server.getInstance().getCommandMap().register("GUIShop",new PlayerOpenShopCommand("gui"));
    }

}
