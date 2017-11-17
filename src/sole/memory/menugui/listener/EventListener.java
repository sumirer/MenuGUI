package sole.memory.menugui.listener;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerLoginEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseModal;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.item.Item;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;
import money.Money;
import sole.memory.menugui.MenuGUI;
import sole.memory.menugui.database.ConfigDataBase;
import sole.memory.menugui.database.PlayerDataBase;
import sole.memory.menugui.lang.Lang;
import sole.memory.menugui.menu.*;
import sole.memory.menugui.menu.data.*;
import sole.memory.menugui.menu.item.ItemName;
import sole.memory.menugui.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by SoleMemory
 * on 2017/10/31.
 */
public class EventListener implements Listener {


    public static HashMap<String,Boolean> isSetPlayer = new HashMap<>();

    private static HashMap<String,AdminSetShop> setStep = new HashMap<>();

    private  HashMap<String,BuyStepData> buyStep = new HashMap<>();

    private  HashMap<String,SellStepData> sellStep = new HashMap<>();

    private HashMap<String,HashMap<String,Object>> commandStep = new HashMap<>();

    private HashMap<String,AdminData> adminData = new HashMap<>();

    public static HashMap<String,PlayerDataBase> playerLogin = new HashMap<>();

    private HashMap<String,HashMap<String,Object>> loginList = new HashMap<>();



    private void cleanPlayerData(Player player){
        if (EventListener.setStep.containsKey(player.getName())) {
            EventListener.setStep.remove(player.getName());
            player.sendMessage(TextFormat.GOLD + Lang.translate("gui-admin-cancel"));
        }
        if (EventListener.isSetPlayer.containsKey(player.getName())) {
            EventListener.isSetPlayer.remove(player.getName());
        }
        if (this.buyStep.containsKey(player.getName())) {
            this.buyStep.remove(player.getName());
        }
        if (this.sellStep.containsKey(player.getName())) {
            this.sellStep.remove(player.getName());
        }
        if (this.commandStep.containsKey(player.getName())){
            this.commandStep.remove(player.getName());
        }
        if (this.adminData.containsKey(player.getName())){
            adminData.remove(player.getName());
        }
    }


    @EventHandler
    public void RespondedEvent(PlayerFormRespondedEvent event) {
        Player player = event.getPlayer();
        if (event.wasClosed()) {
            cleanPlayerData(player);
            return;
        }
        FormResponse response = event.getResponse();
        if (response instanceof FormResponseModal) {
            if (((FormResponseModal) response).getClickedButtonText().equals(Lang.translate("gui-button-quite"))) {
                cleanPlayerData(player);
                return;
            }
            //reEdit
            if (((FormResponseModal) response).getClickedButtonText().equals(Lang.translate("gui-button-back-main-page"))) {
                if (isSetPlayer.containsKey(player.getName()) || adminData.containsKey(player.getName())) {
                    player.showFormWindow(AdminSetShop.getMainPage());
                    cleanPlayerData(player);
                    return;
                }
                player.showFormWindow(PlayerBuyShop.getMainPage());
                cleanPlayerData(player);
            }
        }
        if (response instanceof FormResponseSimple) {
            String name = ((FormResponseSimple) response).getClickedButton().getText();
            if (isSetPlayer.containsKey(player.getName()) && !setStep.containsKey(player.getName()) && !adminData.containsKey(player.getName())) {
                AdminSetShop setShop = new AdminSetShop();
                if (Lang.translate("gui-button-set-shop").equals(name)) {
                    player.showFormWindow(AdminSetShop.getTypeChoosePage());
                }
                if (Lang.translate("gui-button-set-sell").equals(name)) {
                    setShop.type = "sell";
                    player.showFormWindow(AdminSetShop.getTypeChoosePage());
                }
                if (Lang.translate("gui-button-set-data").equals(name)) {
                    adminData.put(player.getName(), new AdminData());
                    AdminChangePlayerInfo.showAllOnlinePlayerList(player);
                    return;
                }
                EventListener.setStep.put(player.getName(), setShop);
                return;
            }
            if (isSetPlayer.containsKey(player.getName()) && setStep.containsKey(player.getName()) && (Lang.translate("gui-button-set-add-item").equals(name) || Lang.translate("gui-button-set-del-item").equals(name))) {
                if (Lang.translate("gui-button-set-del-item").equals(name)) {
                    setStep.get(player.getName()).model = "del";
                }
                AdminSetShop m = setStep.get(player.getName());
                if (m.model.equals("add")) {
                    player.showFormWindow(AdminSetShop.getSearchPage());
                }
                if (m.model.equals("del") && m.type.equals("menu")) {
                    AdminSetShop.getAllShopPage(player);
                }
                if (m.model.equals("del") && m.type.equals("sell")) {
                    AdminSetShop.getSellShopAllPage(player);
                }
                return;
            }
            if (Lang.translate("gui-button-open-shop-shop").equals(name)) {
                if (!player.isSurvival()) {
                    player.sendMessage(TextFormat.RED + Lang.translate("game-mode-check-msg"));
                    return;
                }
                player.showFormWindow(PlayerBuyShop.getAllShopPage());
                buyStep.put(player.getName(), new BuyStepData());
                return;
            }
            if (Lang.translate("gui-button-open-shop-sell").equals(name)) {
                if (!player.isSurvival()) {
                    player.sendMessage(TextFormat.RED + Lang.translate("game-mode-check-msg"));
                    return;
                }
                PlayerSellShop.getSellPage(player);
                sellStep.put(player.getName(), new SellStepData());
                return;
            }
            if (Lang.translate("gui-button-open-command").equals(name)) {
                CommandSend.getAllCommandInfoPage(player);
                commandStep.put(player.getName(), new HashMap<>());
                return;
            }
        }
        //admin model
        if (adminSetPlayerDataInfo(response,player)) return;
        //command
        if (playerRunCommand(response,player)) return;
        //set
        if (adminEditShop(response,player)) return;
        //sell
        if (playerSellStep(response,player)) return;
        //buy
        playerBuyStep(response,player);
    }


    private boolean adminSetPlayerDataInfo(FormResponse response, Player player){
        if (adminData.containsKey(player.getName())) {
            if (response instanceof FormResponseSimple) {
                String name = ((FormResponseSimple) response).getClickedButton().getText();
                AdminData mm = adminData.get(player.getName());
                if (mm.checkInventory) {
                    if (Server.getInstance().getPlayer(mm.user) == null) {
                        //check player is online
                        player.showFormWindow(AdminChangePlayerInfo.getPlayerNotOnlinePage(mm.user));
                        return true;
                    }
                    adminData.get(player.getName()).itemCount = Server.getInstance().getPlayer(mm.user).getInventory().getItem(StringUtils.getButtonItemIndex(name)).count;
                    adminData.get(player.getName()).itemIndex = StringUtils.getButtonItemIndex(name);
                    adminData.get(player.getName()).itemID = StringUtils.getButtonItemID(name);
                    player.showFormWindow(AdminChangePlayerInfo.getItemEditPage(mm.user, adminData.get(player.getName()).itemCount));
                    return true;
                }
                if (Server.getInstance().getPlayer(name.toLowerCase()) == null) {
                    player.showFormWindow(AdminChangePlayerInfo.getPlayerNotOnlinePage(((FormResponseSimple) response).getClickedButton().getText()));
                    cleanPlayerData(player);
                    return true;
                }
                Player player1 = Server.getInstance().getPlayer(name.toLowerCase());
                player.showFormWindow(AdminChangePlayerInfo.getPlayerInfoPage(player1));
                adminData.get(player.getName()).inputPlayer(player1);
            }
            if (response instanceof FormResponseCustom) {
                AdminData data = adminData.get(player.getName());
                if (Server.getInstance().getPlayer(data.user) == null) {
                    //check player is online
                    player.showFormWindow(AdminChangePlayerInfo.getPlayerNotOnlinePage(data.user));
                    return true;
                }
                Player player1 = Server.getInstance().getPlayer(data.user);
                //is checkInventory Model
                if (data.checkInventory && data.itemIndex != -100 && data.itemCount != -100) {
                    Item item = player1.getInventory().getItem(data.itemIndex);
                    if (item.getId() == data.itemID[0] && item.getDamage() == data.itemID[1] && item.count == data.itemCount) {
                        int count = StringUtils.StringToInteger(((FormResponseCustom) response).getResponses().get(0).toString());
                        item.setCount(count);
                        player1.getInventory().setItem(data.itemIndex, item);
                        player.showFormWindow(AdminChangePlayerInfo.getEditBackPage(data.user, count));
                    } else {
                        player.showFormWindow(AdminChangePlayerInfo.getUnKnowErrorPage(data.user));
                    }
                    return true;
                }
                data.changeData(((FormResponseCustom) response).getResponses());
                if (data.ban) {
                    player1.setBanned(true);
                    player.showFormWindow(AdminChangePlayerInfo.getBanPlayerPage(data.user));
                    return true;
                }
                if (data.countError) {
                    player.showFormWindow(AdminChangePlayerInfo.getCountErrorPage(data.money + ""));
                    AdminData vvb = new AdminData();
                    vvb.inputPlayer(player1);
                    adminData.put(player.getName(), vvb);
                    return true;
                }
                if (data.changeMoney) Money.getInstance().setMoney(player1, data.money);
                if (data.changeMode) player1.setGamemode(AdminData.getMode(data.mode));
                if (data.changeOp) player1.setOp(data.isOP);
                if (data.changeHealth) player1.setHealth(data.health);
                if (data.kick) player1.kick(Lang.translate("admin-kick-msg"));
                if (data.kill) {
                    EntityDamageEvent ev = new EntityDamageEvent(player1, EntityDamageEvent.DamageCause.SUICIDE, 1000.0F);
                    player1.getServer().getPluginManager().callEvent(ev);
                    if (ev.isCancelled()) {
                        return true;
                    } else {
                        player1.setLastDamageCause(ev);
                        player1.setHealth(0.0F);
                        player1.sendMessage(new TranslationContainer("commands.kill.successful", player1.getName()));
                        return true;
                    }
                }
                if (data.teleport) player.teleport(player1.getLocation());
                if (!data.teleport && !data.kill && !data.kick) {
                    //if checkInventory is true not clean edit data
                    if (data.checkInventory) {
                        player.showFormWindow(AdminChangePlayerInfo.getPlayerInventoryInfoPage(player1));
                        return true;
                    }
                }
                cleanPlayerData(player);
            }
            if (response instanceof FormResponseModal) {
                if (((FormResponseModal) response).getClickedButtonText().equals(Lang.translate("gui-button-re-edit"))) {
                    AdminData data = adminData.get(player.getName());
                    if (Server.getInstance().getPlayer(data.user) == null) {
                        //check player is online
                        player.showFormWindow(AdminChangePlayerInfo.getPlayerNotOnlinePage(data.user));
                        return true;
                    }
                    player.showFormWindow(AdminChangePlayerInfo.getPlayerInfoPage(Server.getInstance().getPlayer(data.user.toLowerCase())));
                }
                if (((FormResponseModal) response).getClickedButtonText().equals(Lang.translate("gui-button-back-inventory"))) {

                    AdminData data = adminData.get(player.getName());
                    if (Server.getInstance().getPlayer(data.user) == null) {
                        //check player is online
                        player.showFormWindow(AdminChangePlayerInfo.getPlayerNotOnlinePage(data.user));
                        return true;
                    }
                    //clean after item data
                    data.itemID = null;
                    data.itemCount = -100;
                    data.itemIndex = -100;
                    adminData.put(player.getName(), data);
                    player.showFormWindow(AdminChangePlayerInfo.getPlayerInventoryInfoPage(Server.getInstance().getPlayer(data.user)));
                }
            }
            return true;
        }
        return false;
    }

    private boolean playerRunCommand(FormResponse response,Player player){

        if (commandStep.containsKey(player.getName())) {
            if (response instanceof FormResponseSimple) {
                Command command = Server.getInstance().getCommandMap().getCommand(((FormResponseSimple) response).getClickedButton().getText());
                commandStep.get(player.getName()).put("command", command);
                commandStep.get(player.getName()).put("commandString", "");
                player.showFormWindow(CommandSend.getCommandInputPage(command));
                return true;
            }
            if (response instanceof FormResponseCustom) {
                HashMap<Integer, Object> nn = ((FormResponseCustom) response).getResponses();
                String input = nn.size() > 0 ? nn.get(0).toString() : " ";
                Command v = (Command) commandStep.get(player.getName()).get("command");
                //not input value
                if (input == null || " ".equals(input)) {
                    Server.getInstance().getCommandMap().dispatch(player, v.getName());
                    commandStep.remove(player.getName());
                    return true;
                }
                Server.getInstance().getCommandMap().dispatch(player, v.getName() + " " + StringUtils.getCommand(input));
                commandStep.remove(player.getName());
            }
            return true;
        }
        return false;
    }

    private boolean adminEditShop(FormResponse response,Player player){
        if (EventListener.isSetPlayer.containsKey(player.getName())) {
            if (!EventListener.setStep.containsKey(player.getName())) return false;
            AdminSetShop adminSetShop = EventListener.setStep.get(player.getName());
            //model: 1  mode: add type: menu/sell
            if (adminSetShop.model.equals("add")) {
                if (response instanceof FormResponseCustom) {
                    if (adminSetShop.name.equals("")) {
                        String name = ItemName.getName(((FormResponseCustom) response).getInputResponse(1));
                        if ("不存在此物品".equals(name)) {
                            player.showFormWindow(AdminSetShop.getSearchNullPage(name));
                            return true;
                        }
                        EventListener.setStep.get(player.getName()).name = name;
                        EventListener.setStep.get(player.getName()).id = ((FormResponseCustom) response).getInputResponse(1);
                        player.showFormWindow(adminSetShop.getInputPricePage());
                        return true;
                    }
                    if (adminSetShop.price == 0) {
                        String count = ((FormResponseCustom) response).getInputResponse(1);
                        if (StringUtils.isNumeric(count) && Integer.valueOf(count) > 0) {
                            EventListener.setStep.get(player.getName()).price = Float.valueOf((((FormResponseCustom) response).getInputResponse(1)));
                            player.showFormWindow(adminSetShop.getCheckPage());
                        } else {
                            player.showFormWindow(adminSetShop.getInputPricePage());
                        }
                    }
                }
                if (response instanceof FormResponseModal) {
                    if (((FormResponseModal) response).getClickedButtonText().equals(Lang.translate("gui-button-reenter"))) {
                        player.showFormWindow(AdminSetShop.getSearchPage());
                        return true;
                    }
                    if (((FormResponseModal) response).getClickedButtonText().equals(Lang.translate("gui-button-cancel"))) {
                        player.showFormWindow(adminSetShop.getFailedPage());
                        return true;
                    }
                    if (((FormResponseModal) response).getClickedButtonText().equals(Lang.translate("gui-button-determine"))) {
                        if (adminSetShop.isCheckData()) {
                            if (adminSetShop.type.equals("menu")) {
                                ConfigDataBase.addNewData(adminSetShop);
                                player.showFormWindow(adminSetShop.getSuccessPage());
                            } else {
                                ConfigDataBase.addNewSellData(adminSetShop);
                                player.showFormWindow(adminSetShop.getSuccessSellPage());
                            }
                            return true;
                        }
                        player.showFormWindow(adminSetShop.getFailedPage());
                    }
                }
                return true;
            }
            //model: 2  mode: del type: menu/sell
            if (adminSetShop.model.equals("del")) {
                if (adminSetShop.type.equals("menu")) {
                    if (response instanceof FormResponseModal) {
                        if (((FormResponseModal) response).getClickedButtonText().equals(Lang.translate("gui-button-re-edit"))) {
                            player.showFormWindow(AdminSetShop.getEditShopPage(setStep.get(player.getName()).shopData));
                            return true;
                        }
                        if (((FormResponseModal) response).getClickedButtonText().equals(Lang.translate("gui-button-cancel-edit"))) {
                            player.showFormWindow(AdminSetShop.getEditFailedPage());
                        }
                    }
                    if (response instanceof FormResponseCustom) {
                        if (adminSetShop.shopData != null) {
                            HashMap<Integer, Object> check = ((FormResponseCustom) response).getResponses();
                            String name = check.get(1).toString();
                            String id = check.get(2).toString();
                            String input1 = check.get(0).toString();
                            boolean delete = (boolean) check.get(3);
                            if (delete) {
                                //delete this item
                                ConfigDataBase.deleteData(adminSetShop.shopData.index);
                                player.showFormWindow(AdminSetShop.getDeletePage("出售", adminSetShop.shopData.index));
                                return true;
                            }
                            if (!AdminSetShop.checkInputID(id)) {
                                player.showFormWindow(AdminSetShop.getIDErrorPage(id));
                                return true;
                            }
                            if (!StringUtils.isNumeric(input1)) {
                                player.showFormWindow(AdminSetShop.getPriceErrorPage(input1));
                                return true;
                            }
                            float price = Float.valueOf(input1);

                            ShopData cc = adminSetShop.shopData;
                            cc.price = price;
                            cc.name = name;
                            cc.id = id;
                            ConfigDataBase.updateShopData(cc.index, cc);
                            player.showFormWindow(AdminSetShop.getEditSuccessPage());
                            isSetPlayer.remove(player.getName());
                            setStep.remove(player.getName());
                            return true;
                        }
                        String index = StringUtils.getShopIndexByButtonText(((FormResponseCustom) response).getDropdownResponse(1).getElementContent());
                        ShopData data = ConfigDataBase.getShopDataByIndex(index);
                        setStep.get(player.getName()).shopData = data;
                        //edit page
                        player.showFormWindow(AdminSetShop.getEditShopPage(data));
                    }
                }
                if (adminSetShop.type.equals("sell")) {
                    if (response instanceof FormResponseModal) {
                        if (((FormResponseModal) response).getClickedButtonText().equals(Lang.translate("gui-button-re-edit"))) {
                            player.showFormWindow(AdminSetShop.getEditSellShopPage(setStep.get(player.getName()).sellData));
                            return true;
                        }
                        if (((FormResponseModal) response).getClickedButtonText().equals(Lang.translate("gui-button-cancel-edit"))) {
                            player.showFormWindow(AdminSetShop.getEditFailedPage());
                        }
                    }
                    if (response instanceof FormResponseCustom) {
                        if (adminSetShop.sellData != null) {
                            HashMap<Integer, Object> check = ((FormResponseCustom) response).getResponses();
                            String name = check.get(1).toString();
                            String id = check.get(2).toString();
                            String input1 = check.get(0).toString();
                            boolean delete = (boolean) check.get(3);
                            if (delete) {
                                //delete this item
                                ConfigDataBase.deleteSellData(adminSetShop.sellData.index);
                                player.showFormWindow(AdminSetShop.getDeletePage("回收", adminSetShop.sellData.index));
                                isSetPlayer.remove(player.getName());
                                setStep.remove(player.getName());
                                return true;
                            }
                            if (!AdminSetShop.checkInputID(id)) {
                                player.showFormWindow(AdminSetShop.getIDErrorPage(id));
                                return true;
                            }
                            if (!StringUtils.isNumeric(input1)) {
                                player.showFormWindow(AdminSetShop.getPriceErrorPage(input1));
                                return true;
                            }
                            float price = Float.valueOf(input1);
                            SellData cc = adminSetShop.sellData;
                            cc.price = price;
                            cc.name = name;
                            cc.id = id;
                            ConfigDataBase.updateSellData(cc.index, cc);
                            player.showFormWindow(AdminSetShop.getEditSuccessPage());
                            return true;
                        }
                        String index = StringUtils.getShopIndexByButtonText(((FormResponseCustom) response).getDropdownResponse(1).getElementContent());
                        SellData data = ConfigDataBase.getSellDataByIndex(index);
                        setStep.get(player.getName()).sellData = data;
                        //edit page
                        player.showFormWindow(AdminSetShop.getEditSellShopPage(data));
                    }
                }
            }
            return true;
        }
        return false;
    }


    private boolean playerSellStep(FormResponse response,Player player){
        if (sellStep.containsKey(player.getName())) {
            if (response instanceof FormResponseCustom) {
                if (sellStep.get(player.getName()).data == null) {
                    SellData cv = ConfigDataBase.getSellDataByIndex(StringUtils.getSellInfoIndexText(((FormResponseCustom) response).getDropdownResponse(1).getElementContent()));
                    sellStep.get(player.getName()).data = cv;
                    player.showFormWindow(PlayerSellShop.getSellShopChoseCountPage(PlayerSellShop.getMaxCount(player, cv.id)));
                    return true;
                }
                int count = (int) ((FormResponseCustom) response).getSliderResponse(0);
                sellStep.get(player.getName()).sellCount = count;
                sellStep.get(player.getName()).getPrice();
                player.showFormWindow(PlayerSellShop.getSellCheckPage(sellStep.get(player.getName()).data, count));
            }
            if (response instanceof FormResponseModal) {
                //success
                if (((FormResponseModal) response).getClickedButtonText().equals(Lang.translate("gui-button-determine"))) {
                    float price = sellStep.get(player.getName()).price;
                    player.showFormWindow(PlayerSellShop.getSellSuccessPage(price));
                    Money.getInstance().addMoney(player, price);
                    Integer[] lk = StringUtils.getItemInfo(sellStep.get(player.getName()).data.id);
                    Item item = Item.get(lk[0], lk[1]);
                    item.setCount(sellStep.get(player.getName()).sellCount);
                    player.getInventory().removeItem(item);
                    return true;
                }
                //cancel
                player.showFormWindow(PlayerSellShop.getSellCancelPage());
            }
            return true;
        }
        return false;
    }


    private void playerBuyStep(FormResponse response,Player player) {
        if (this.buyStep.containsKey(player.getName())) {
            if (response instanceof FormResponseCustom) {
                if (!this.buyStep.get(player.getName()).isChooseCount() && buyStep.get(player.getName()).data != null) {
                    int count = (int) ((FormResponseCustom) response).getSliderResponse(1);
                    //check player's money
                    ShopData data = this.buyStep.get(player.getName()).getData();
                    if (!PlayerBuyShop.playerHaveMoney(player, data.price * count)) {
                        player.showFormWindow(PlayerBuyShop.getNoMoneyPage(data, count));
                        this.buyStep.remove(player.getName());
                        return;
                    }
                    this.buyStep.get(player.getName()).chooseCount = true;
                    this.buyStep.get(player.getName()).count = count;
                    player.showFormWindow(PlayerBuyShop.getBuyCheckPage(this.buyStep.get(player.getName()).getData(), count));
                    return;
                }
                String index = StringUtils.getShopIndexByButtonText(((FormResponseCustom) response).getDropdownResponse(1).getElementContent());
                if (ConfigDataBase.isDataIndex(index)) {
                    BuyStepData buyData = new BuyStepData();
                    buyData.data = ConfigDataBase.getShopDataByIndex(index);
                    this.buyStep.put(player.getName(), buyData);
                    //show buy page
                    player.showFormWindow(PlayerBuyShop.getBuyPage(buyData.data));
                }
            }

            if (response instanceof FormResponseModal && this.buyStep.get(player.getName()).isChooseCount()) {
                BuyStepData data = this.buyStep.get(player.getName());
                if (((FormResponseModal) response).getClickedButtonText().equals(Lang.translate("gui-button-determine"))) {
                    Integer[] lk = StringUtils.getItemInfo(data.data.id);
                    Item item = Item.get(lk[0], lk[1]);
                    item.setCount(data.count);
                    if (!player.getInventory().canAddItem(item)) {
                        player.showFormWindow(PlayerBuyShop.getFoolPage(data.data, data.count));
                        return;
                    }
                    player.getInventory().addItem(item);
                    Money.getInstance().reduceMoney(player, data.data.price * data.count);
                    player.showFormWindow(PlayerBuyShop.getBuySuccessPage(data.data, data.getCount()));
                }
            }
        }
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        PlayerDataBase dataBase = new PlayerDataBase(player.getName().toLowerCase());
        playerLogin.put(player.getName(),dataBase);
        if (dataBase.isNewPlayer){
            PlayerLoginSend.getPlayerRegisterPage(player);
            return;
        }
        if (dataBase.isTowHourNoCheck()){
            // set login
            playerLogin.remove(player.getName());
        }
    }


    @EventHandler
    public void playerLogin(PlayerLoginEvent event){
        if (MenuGUI.banList.exists(event.getPlayer().getName().toLowerCase())){
            event.setCancelled();
            event.setKickMessage(TextFormat.RED+"You has been Ban,Please contact the administrator");
        }
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event){
        //must be lowerCase
        String player = event.getPlayer().getName().toLowerCase();
        if (playerLogin.containsKey(player)){
            PlayerDataBase dataBase = playerLogin.get(player);
            if (!dataBase.isNewPlayer){
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                dataBase.lastDate = df.format(new Date());
                dataBase.save();
            }
        }
    }
}
