package sole.memory.menugui.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import sole.memory.menugui.listener.EventListener;
import sole.memory.menugui.menu.AdminSetShop;

/**
 * Created by SoleMemory
 * on 2017/11/1.
 */
public class AdminAddItemCommand extends Command {
    public AdminAddItemCommand(String name) {
        super(name,"gui menu admin add/del menu item command","");
        this.setPermission("sole.memory.gui.admin");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(TextFormat.RED + "请在游戏内执行此命令");
            return true;
        }
        if (!commandSender.hasPermission(this.getPermission())) {
            commandSender.sendMessage(TextFormat.RED + "你没有权限执行此命令");
            return true;
        }
        ((Player) commandSender).showFormWindow(AdminSetShop.getMainPage());
        EventListener.isSetPlayer.put(commandSender.getName(), true);
        return true;
    }
}
