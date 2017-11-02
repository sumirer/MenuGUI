package sole.memory.guishop.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseModal;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.item.Item;
import cn.nukkit.utils.TextFormat;
import money.Money;
import sole.memory.guishop.GUIShop;
import sole.memory.guishop.database.ConfigDataBase;
import sole.memory.guishop.shop.AdminSetShop;
import sole.memory.guishop.shop.PlayerBuyShop;
import sole.memory.guishop.shop.PlayerSellShop;
import sole.memory.guishop.shop.data.BuyStepData;
import sole.memory.guishop.shop.data.SellData;
import sole.memory.guishop.shop.data.SellStepData;
import sole.memory.guishop.shop.data.ShopData;
import sole.memory.guishop.shop.item.ItemName;
import sole.memory.guishop.utils.StringUtils;

import java.util.HashMap;

/**
 * Created by SoleMemory
 * on 2017/10/31.
 */
public class EventListener extends GUIShop implements Listener {


    public static HashMap<String,Boolean> isSetPlayer = new HashMap<>();

    public static HashMap<String,AdminSetShop> setStep = new HashMap<>();

    private  HashMap<String,BuyStepData> buyStep = new HashMap<>();

    private  HashMap<String,SellStepData> sellStep = new HashMap<>();



    private void cleanPlayerData(Player player){
        if (EventListener.setStep.containsKey(player.getName())) {
            EventListener.setStep.remove(player.getName());
            player.sendMessage(TextFormat.GOLD + "你已经取消添加");
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
    }

    @EventHandler
    public void RespondedEvent(PlayerFormRespondedEvent event) {
        Player player = event.getPlayer();
        if (event.wasClosed()) {
           cleanPlayerData(player);
            return;
        }
        FormResponse response = event.getResponse();
        if (response instanceof FormResponseModal){
            if (((FormResponseModal) response).getClickedButtonText().equals("退出")){
               cleanPlayerData(player);
                return;
            }
        }
        if (response instanceof FormResponseSimple){
            String name = ((FormResponseSimple) response).getClickedButton().getText();
            if (isSetPlayer.containsKey(player.getName()) && !setStep.containsKey(player.getName())){
                AdminSetShop setShop = new AdminSetShop();
                if ("设置出售商店".equals(name)) {
                    player.showFormWindow(AdminSetShop.getTypeChoosePage());
                }
                if ("设置回收商店".equals(name)) {
                    setShop.type = "sell";
                    player.showFormWindow(AdminSetShop.getTypeChoosePage());
                }
                EventListener.setStep.put(player.getName(),setShop);
                return;
            }
            if (isSetPlayer.containsKey(player.getName()) && setStep.containsKey(player.getName()) && ("添加物品".equals(name)||"删除物品".equals(name))){
                if ("删除物品".equals(name)){
                    setStep.get(player.getName()).model = "del";
                }
                AdminSetShop m = setStep.get(player.getName());
                if (m.model.equals("add")){
                    player.showFormWindow(AdminSetShop.getSearchPage());
                }
                if (m.model.equals("del") && m.type.equals("shop")){
                    AdminSetShop.getAllShopPage(player);
                }
                if (m.model.equals("del") && m.type.equals("sell")){
                    AdminSetShop.getSellShopAllPage(player);
                }
                return;
            }
            if ("打开出售商店".equals(name)) {
                player.showFormWindow(PlayerBuyShop.getAllShopPage());
                buyStep.put(player.getName(),new BuyStepData());
                return;
            }
            if ("打开回收商店".equals(name)){
                PlayerSellShop.getSellPage(player);
                sellStep.put(player.getName(),new SellStepData());
                return;
            }
        }
        if (EventListener.isSetPlayer.containsKey(player.getName())) {
            if (!EventListener.setStep.containsKey(player.getName())) return;
            AdminSetShop adminSetShop = EventListener.setStep.get(player.getName());
            //model: 1  mode: add type: shop/sell
            if (adminSetShop.model.equals("add")) {
                if (response instanceof FormResponseCustom) {
                    if (adminSetShop.name.equals("")) {
                        String name = ItemName.getName(((FormResponseCustom) response).getInputResponse(1));
                        if ("不存在此物品".equals(name)) {
                            player.showFormWindow(AdminSetShop.getSearchNullPage(name));
                            return;
                        }
                        EventListener.setStep.get(player.getName()).name = name;
                        EventListener.setStep.get(player.getName()).id = ((FormResponseCustom) response).getInputResponse(1);
                        player.showFormWindow(adminSetShop.getInputPricePage());
                        return;
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
                    if (((FormResponseModal) response).getClickedButtonText().equals("重新输入")) {
                        player.showFormWindow(AdminSetShop.getSearchPage());
                        return;
                    }
                    if (((FormResponseModal) response).getClickedButtonText().equals("取消添加")) {
                        player.showFormWindow(adminSetShop.getFailedPage());
                        return;
                    }
                    if (((FormResponseModal) response).getClickedButtonText().equals("确认")) {
                        if (adminSetShop.isCheckData()) {
                            if (adminSetShop.type.equals("shop")) {
                                ConfigDataBase.addNewData(adminSetShop);
                                player.showFormWindow(adminSetShop.getSuccessPage());
                            }else {
                                ConfigDataBase.addNewSellData(adminSetShop);
                                player.showFormWindow(adminSetShop.getSuccessSellPage());
                            }

                            EventListener.isSetPlayer.remove(player.getName());
                            EventListener.setStep.remove(player.getName());
                            return;
                        }
                        player.showFormWindow(adminSetShop.getFailedPage());
                    }
                }
                return;
            }
            //model: 2  mode: del type: shop/sell
            if (adminSetShop.model.equals("del")){
                if (adminSetShop.type.equals("shop")){
                    if (response instanceof FormResponseModal){
                        if (((FormResponseModal) response).getClickedButtonText().equals("重新编辑")){
                            player.showFormWindow(AdminSetShop.getEditShopPage(setStep.get(player.getName()).shopData));
                            return;
                        }
                        if (((FormResponseModal) response).getClickedButtonText().equals("取消编辑")){
                            player.showFormWindow(AdminSetShop.getEditFailedPage());
                            isSetPlayer.remove(player.getName());
                            setStep.remove(player.getName());
                        }
                    }
                    if (response instanceof FormResponseCustom){
                        if (adminSetShop.shopData!=null){
                            HashMap<Integer,Object> check = ((FormResponseCustom) response).getResponses();
                            String name = check.get(1).toString();
                            String id = check.get(2).toString();
                            String input1 = check.get(0).toString();
                            boolean delete = (boolean)check.get(3);
                            if (delete){
                                //delete this item
                                ConfigDataBase.deleteData(adminSetShop.shopData.index);
                                player.showFormWindow(AdminSetShop.getDeletePage("出售",adminSetShop.shopData.index));
                                isSetPlayer.remove(player.getName());
                                setStep.remove(player.getName());
                                return;
                            }
                            if (!AdminSetShop.checkInputID(id)){
                                //TODO:: Model
                                player.showFormWindow(AdminSetShop.getIDErrorPage(id));
                                return;
                            }
                            if (!StringUtils.isNumeric(input1)){
                                player.showFormWindow(AdminSetShop.getPriceErrorPage(input1));
                                return;
                            }
                            float price = Float.valueOf(input1);

                            ShopData cc = adminSetShop.shopData;
                            cc.price = price;
                            cc.name = name;
                            cc.id = id;
                            ConfigDataBase.updateShopData(cc.index,cc);
                            player.showFormWindow(AdminSetShop.getEditSuccessPage());
                            isSetPlayer.remove(player.getName());
                            setStep.remove(player.getName());
                            return;
                        }
                        String index = StringUtils.getShopIndexByButtonText(((FormResponseCustom) response).getDropdownResponse(1).getElementContent());
                        ShopData data = ConfigDataBase.getShopDataByIndex(index);
                        setStep.get(player.getName()).shopData = data;
                        //edit page
                        player.showFormWindow(AdminSetShop.getEditShopPage(data));
                    }
                }
                if (adminSetShop.type.equals("sell")){
                    if (response instanceof FormResponseModal){
                        if (((FormResponseModal) response).getClickedButtonText().equals("重新编辑")){
                            player.showFormWindow(AdminSetShop.getEditSellShopPage(setStep.get(player.getName()).sellData));
                            return;
                        }
                        if (((FormResponseModal) response).getClickedButtonText().equals("取消编辑")){
                            player.showFormWindow(AdminSetShop.getEditFailedPage());
                            isSetPlayer.remove(player.getName());
                            setStep.remove(player.getName());
                        }
                    }
                    if (response instanceof FormResponseCustom){
                        if (adminSetShop.sellData!=null){
                            HashMap<Integer,Object> check = ((FormResponseCustom) response).getResponses();
                            String name = check.get(1).toString();
                            String id = check.get(2).toString();
                            String input1 = check.get(0).toString();
                            boolean delete = (boolean)check.get(3);
                            if (delete){
                                //delete this item
                                ConfigDataBase.deleteSellData(adminSetShop.sellData.index);
                                player.showFormWindow(AdminSetShop.getDeletePage("回收",adminSetShop.sellData.index));
                                isSetPlayer.remove(player.getName());
                                setStep.remove(player.getName());
                                return;
                            }
                            if (!AdminSetShop.checkInputID(id)){
                                player.showFormWindow(AdminSetShop.getIDErrorPage(id));
                                return;
                            }
                            if (!StringUtils.isNumeric(input1)){
                                player.showFormWindow(AdminSetShop.getPriceErrorPage(input1));
                                return;
                            }
                            float price = Float.valueOf(input1);
                            SellData cc = adminSetShop.sellData;
                            cc.price = price;
                            cc.name = name;
                            cc.id = id;
                            ConfigDataBase.updateSellData(cc.index,cc);
                            player.showFormWindow(AdminSetShop.getEditSuccessPage());
                            isSetPlayer.remove(player.getName());
                            setStep.remove(player.getName());
                            return;
                        }
                        String index = StringUtils.getShopIndexByButtonText(((FormResponseCustom) response).getDropdownResponse(1).getElementContent());
                        SellData data = ConfigDataBase.getSellDataByIndex(index);
                        setStep.get(player.getName()).sellData = data;
                        //edit page
                        player.showFormWindow(AdminSetShop.getEditSellShopPage(data));
                    }
                }
            }
        }
        //sell
        if (sellStep.containsKey(player.getName())) {
            if (response instanceof FormResponseCustom) {
                if (sellStep.get(player.getName()).data == null) {
                    SellData cv = ConfigDataBase.getSellDataByIndex(StringUtils.getSellInfoIndexText(((FormResponseCustom) response).getDropdownResponse(1).getElementContent()));
                    sellStep.get(player.getName()).data = cv;
                    player.showFormWindow(PlayerSellShop.getSellShopChoseCountPage(PlayerSellShop.getMaxCount(player,cv.id)));
                    return;
                }
                int count = (int) ((FormResponseCustom) response).getSliderResponse(0);
                sellStep.get(player.getName()).sellCount = count;
                sellStep.get(player.getName()).getPrice();
                player.showFormWindow(PlayerSellShop.getSellCheckPage(sellStep.get(player.getName()).data, count));
            }
            if (response instanceof FormResponseModal) {
                //success
                if (((FormResponseModal) response).getClickedButtonText().equals("确认")) {
                    float price = sellStep.get(player.getName()).price;
                    player.showFormWindow(PlayerSellShop.getSellSuccessPage(price));
                    Money.getInstance().addMoney(player, price);
                    Integer[] lk = StringUtils.getItemInfo(sellStep.get(player.getName()).data.id);
                    Item item = Item.get(lk[0],lk[1]);
                    item.setCount(sellStep.get(player.getName()).sellCount);
                    player.getInventory().removeItem(item);
                    sellStep.remove(player.getName());
                    return;
                }
                //cancel
                player.showFormWindow(PlayerSellShop.getSellCancelPage());
                sellStep.remove(player.getName());
            }
            return;
        }
        //buy
        if (this.buyStep.containsKey(player.getName())) {
            if (response instanceof FormResponseCustom) {
                if (!this.buyStep.get(player.getName()).isChooseCount()&& buyStep.get(player.getName()).data!=null) {
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
                if (((FormResponseModal) response).getClickedButtonText().equals("确认")) {
                    Integer[] lk = StringUtils.getItemInfo(data.data.id);
                    Item item = Item.get(lk[0], lk[1]);
                    item.setCount(data.count);
                    if (!player.getInventory().canAddItem(item)) {
                        player.showFormWindow(PlayerBuyShop.getFoolPage(data.data, data.count));
                        this.buyStep.remove(player.getName());
                        return;
                    }
                    player.getInventory().addItem(item);
                    Money.getInstance().reduceMoney(player, data.data.price * data.count);
                    player.showFormWindow(PlayerBuyShop.getBuySuccessPage(data.data, data.getCount()));
                }
            }

        }
    }
}
