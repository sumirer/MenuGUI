package sole.memory.menugui.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;
import sole.memory.menugui.menu.PlayerBuyShop;

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
            commandSender.sendMessage(new TranslationContainer(TextFormat.RED+"%commands.generic.permission"));
            return true;
        }
        ((Player) commandSender).showFormWindow(PlayerBuyShop.getMainPage());
        return true;
    }
}
