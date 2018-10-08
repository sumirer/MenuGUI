package sole.memory.menugui;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.form.window.FormWindowModal;
import cn.nukkit.permission.Permission;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import sole.memory.menugui.command.AdminAddItemCommand;
import sole.memory.menugui.command.LevelTeleportCommand;
import sole.memory.menugui.command.PlayerOpenShopCommand;
import sole.memory.menugui.database.ConfigDataBase;
import sole.memory.menugui.lang.Lang;
import sole.memory.menugui.listener.EventListener;
import sole.memory.menugui.menu.data.LevelData;
import sole.memory.menugui.windows.WindowsType;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * on 2017/10/31.
 * @author SoleMemory
 */
public class MenuGUI extends PluginBase implements MenuAPI{

    private static MenuGUI plugin = null;





    public static MenuGUI getInstance(){
        return MenuGUI.plugin;
    }
    private boolean haveSimpleLand = false;
    @Override
    public void onEnable() {
        MenuGUI.plugin = this;
        this.getDataFolder().mkdirs();
        File file = new File(getDataFolder()+"/imgPath");
        if (!file.exists() && file.isDirectory()){
            if (!file.mkdir()){
                this.getLogger().warning("The plugin's data folder can't mkdir");
            }
        }
        File file1 = new File(getDataFolder()+"/player");
        if (!file1.exists() && file1.isDirectory()){
            if (!file1.mkdir()){
                this.getLogger().warning("The player's data folder can't mkdir");
            }
        }
        this.saveDefaultConfig();
        this.saveResource("imgPath/set.png",false);
        this.saveResource("imgPath/shop.jpg",false);
        //check value
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(new String[]{"chs","cht","eng"}));
        Lang.init(arrayList.contains(this.getConfig().getString("Language","chs"))?this.getConfig().getString("Language","chs"):"chs");
        this.getLogger().info("this just test gui");
        Server.getInstance().getPluginManager().registerEvents(new EventListener(),this);
        ConfigDataBase.initData();
        registerPermission();
        registerCommand();
        LevelData.init();
        checkSimpleLand();
        this.getLogger().info("this plugin is load.....");
    }

    public boolean haveSimpleLand(){
        return  haveSimpleLand;
    }
    private void checkSimpleLand(){
            Plugin simpleLand = Server.getInstance().getPluginManager().getPlugin("SimpleLand");
        if (simpleLand!= null && simpleLand.isEnabled()){
            haveSimpleLand = true;
        }
    }
    private void registerPermission(){
        Server.getInstance().getPluginManager().addPermission(new Permission("sole.memory.gui.admin","op"));
        Server.getInstance().getPluginManager().addPermission(new Permission("sole.memory.gui.player","true"));
    }


    private void registerCommand(){
        Server.getInstance().getCommandMap().register("MenuGUI",new AdminAddItemCommand("gui"));
        Server.getInstance().getCommandMap().register("MenuGUI",new PlayerOpenShopCommand("menu"));
        Server.getInstance().getCommandMap().register("MenuGUI",new LevelTeleportCommand("w"));
    }





}
