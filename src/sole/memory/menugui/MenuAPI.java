package sole.memory.menugui;

import cn.nukkit.Player;
import sole.memory.menugui.windows.WindowsType;

/**
 * Created by SoleMemory
 * on 2017/11/17.
 */
public interface  MenuAPI {

    static MenuGUI getInstance(){
        return MenuGUI.getInstance();
    }

   boolean isLogin(Player player);

   void setLogin(Player player);

   void sendGuiMessageToPlayer(Player player, WindowsType windowsType, String msg);

   void sendEmailToPlayer(String account, String user, String title, String msg);

   void sendEmailCheckToPlayer(String player, String verify, String type, String mailAccount);
}
