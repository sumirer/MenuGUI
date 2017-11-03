package sole.memory.guishop.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import sole.memory.guishop.menu.PlayerBuyShop;

/**
 * Created by SoleMemory
 * on 2017/11/1.
 */
public class PlayerOpenShopCommand extends Command{


    public PlayerOpenShopCommand(String name) {
        super(name,  "open gui menu","");
        this.setPermission("sole.memory.gui.player");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {

        if (!(commandSender instanceof Player)){
            commandSender.sendMessage(TextFormat.RED+"请在游戏内执行此命令");
            return true;
        }
        ((Player) commandSender).showFormWindow(PlayerBuyShop.getMainPage());
        return true;
    }
}
