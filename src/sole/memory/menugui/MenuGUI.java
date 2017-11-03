package sole.memory.menugui;

import cn.nukkit.Server;
import cn.nukkit.permission.Permission;
import cn.nukkit.plugin.PluginBase;
import sole.memory.menugui.command.AdminAddItemCommand;
import sole.memory.menugui.command.PlayerOpenShopCommand;
import sole.memory.menugui.database.ConfigDataBase;
import sole.memory.menugui.listener.EventListener;

/**
 *
 * on 2017/10/31.
 * @author SoleMemory
 */
public class MenuGUI extends PluginBase{

    private static MenuGUI plugin = null;

    public static MenuGUI getInstance(){
        return MenuGUI.plugin;
    }

    @Override
    public void onEnable() {
        MenuGUI.plugin = this;
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
        Server.getInstance().getCommandMap().register("MenuGUI",new AdminAddItemCommand("gshop"));
        Server.getInstance().getCommandMap().register("MenuGUI",new PlayerOpenShopCommand("gui"));
    }

}
