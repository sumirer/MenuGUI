package sole.memory.menugui.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import sole.memory.menugui.listener.EventListener;
import sole.memory.menugui.menu.PlayerTeleportSend;

public class LevelTeleportCommand extends Command {
    public LevelTeleportCommand(String name) {
        super(name, "世界传送命令");
        this.setPermission("sole.memory.gui.player");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (commandSender instanceof Player) {
            PlayerTeleportSend.sendAllCanTPLevel((Player) commandSender);
            EventListener.world.put(commandSender.getName(),true);
        }
        return false;
    }
}
