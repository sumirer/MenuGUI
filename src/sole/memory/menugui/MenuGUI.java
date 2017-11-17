package sole.memory.menugui;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.form.window.FormWindowModal;
import cn.nukkit.permission.Permission;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import sole.memory.menugui.command.AdminAddItemCommand;
import sole.memory.menugui.command.PlayerOpenShopCommand;
import sole.memory.menugui.database.ConfigDataBase;
import sole.memory.menugui.email.Email;
import sole.memory.menugui.lang.Lang;
import sole.memory.menugui.listener.EventListener;
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

    public static Config banList;

    public static ConfigSection mail;

    public ExecutorService threadPool;



    public static MenuGUI getInstance(){
        return MenuGUI.plugin;
    }

    @Override
    public void onEnable() {
        MenuGUI.plugin = this;
        this.getDataFolder().mkdirs();
        //start new thread pool ,thread count is tow ,Restricted send speed
        threadPool = Executors.newFixedThreadPool(2);
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
        this.saveResource("email.yml",false);
        //check value
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(new String[]{"chs","cht","eng"}));
        Lang.init(arrayList.contains(this.getConfig().getString("Language","chs"))?this.getConfig().getString("Language","chs"):"chs");
        banList = new Config(plugin.getDataFolder()+"/ban.yml",Config.YAML);
        mail = new ConfigSection(plugin.getDataFolder()+"/email.yml",Config.YAML);
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

    @Override
    public boolean isLogin(Player player) {
        return !EventListener.playerLogin.containsKey(player.getName());
    }

    @Override
    public void setLogin(Player player) {
        if (EventListener.playerLogin.containsKey(player.getName())){
            EventListener.playerLogin.remove(player.getName());
        }
    }


    private void sendGuiMessageToPlayer(Player player, String msg) {
        player.showFormWindow(new FormWindowModal("消息",msg,"确定","取消"));
    }

    @Override
    public void sendGuiMessageToPlayer(Player player, WindowsType windowsType, String msg) {
       if (windowsType.getValue().equals(WindowsType.CUSTOM.getValue())){

       }
       if (windowsType.getValue().equals(WindowsType.MODAL.getValue())){
           sendGuiMessageToPlayer(player,msg);
       }
    }

    @Override
    public void sendEmailToPlayer(String account, String user,String title,String msg) {
        threadPool.execute(()->{
            Email email = new Email("","","",account);
            email.sendEmail(true,user,title,msg);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void  sendEmailCheckToPlayer(String player,String verify,String type,String mailAccount){
        threadPool.execute(() -> {
            Email email = new Email(player,verify,type,mailAccount);
            email.sendEmail(false,"","","");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
