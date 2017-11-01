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
import sole.memory.guishop.shop.data.BuyData;
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

    private  HashMap<String,BuyData> buyStep = new HashMap<>();



    @EventHandler
    public void RespondedEvent(PlayerFormRespondedEvent event){
        Player player = event.getPlayer();
        if (event.wasClosed()){
            if (EventListener.setStep.containsKey(player.getName())){
                EventListener.setStep.remove(player.getName());
                player.sendMessage(TextFormat.GOLD+"你已经取消添加");
            }
            if (EventListener.isSetPlayer.containsKey(player.getName())){
                EventListener.isSetPlayer.remove(player.getName());
            }
            if (this.buyStep.containsKey(player.getName())){
                this.buyStep.remove(player.getName());
            }
            return;
        }
        FormResponse response = event.getResponse();
        if (EventListener.isSetPlayer.containsKey(player.getName())) {
            if (!EventListener.setStep.containsKey(player.getName())) return;
            AdminSetShop adminSetShop = EventListener.setStep.get(player.getName());
            if (response instanceof FormResponseCustom) {
                if (adminSetShop.name.equals("")) {
                    EventListener.setStep.get(player.getName()).name = ItemName.getName(((FormResponseCustom) response).getInputResponse(1));
                    EventListener.setStep.get(player.getName()).id = ((FormResponseCustom) response).getInputResponse(1);
                    player.showFormWindow(adminSetShop.getInputPricePage());
                    return;
                }
                if (adminSetShop.price==0){
                    String count = ((FormResponseCustom) response).getInputResponse(1);
                    if (StringUtils.isNumeric(count) && Integer.valueOf(count)>0) {
                        EventListener.setStep.get(player.getName()).price = Float.valueOf((((FormResponseCustom) response).getInputResponse(1)));
                        player.showFormWindow(adminSetShop.getCheckPage());
                    }else {
                        player.showFormWindow(adminSetShop.getInputPricePage());
                    }
                }
            }
            if (response instanceof FormResponseModal){
                if (((FormResponseModal) response).getClickedButtonText().equals("确认")) {
                    if (adminSetShop.isCheckData()) {
                        ConfigDataBase.addNewData(adminSetShop);
                        player.showFormWindow(adminSetShop.getSuccessPage());
                        EventListener.isSetPlayer.remove(player.getName());
                        EventListener.setStep.remove(player.getName());
                        return;
                    }
                    player.showFormWindow(adminSetShop.getFailedPage());
                }
            }
            return;
        }
        if (response instanceof FormResponseSimple){
            String index = StringUtils.getShopIndexByButtonText(((FormResponseSimple) response).getClickedButton().getText());
            if (ConfigDataBase.isDataIndex(index)){
                if (!this.buyStep.containsKey(player.getName())) {
                    BuyData buyData = new BuyData();
                    buyData.data = ConfigDataBase.getShopDataByIndex(index);
                    this.buyStep.put(player.getName(),buyData);
                    //show buy page
                    player.showFormWindow(PlayerBuyShop.getBuyPage(buyData.data));
                }
            }
        }
        if (response instanceof FormResponseCustom && this.buyStep.containsKey(player.getName())){
            if (!this.buyStep.get(player.getName()).isChooseCount()) {
                 int count = (int) ((FormResponseCustom) response).getSliderResponse(1);
                //check player's money
                ShopData data = this.buyStep.get(player.getName()).getData();
                if (!PlayerBuyShop.playerHaveMoney(player, data.price*count)){
                    player.showFormWindow(PlayerBuyShop.getNoMoneyPage(data,count));
                    this.buyStep.remove(player.getName());
                    return;
                }
                this.buyStep.get(player.getName()).chooseCount = true;
                this.buyStep.get(player.getName()).count = count;
                player.showFormWindow(PlayerBuyShop.getBuyCheckPage(this.buyStep.get(player.getName()).getData(),count));
            }
        }

        if (response instanceof FormResponseModal && this.buyStep.containsKey(player.getName()) && this.buyStep.get(player.getName()).isChooseCount()){
            BuyData data = this.buyStep.get(player.getName());
            if (((FormResponseModal) response).getClickedButtonText().equals("确认")){
                String[] ids = data.data.id.split(":");
                int id;
                int mate;
                if (ids.length==1){
                    id = Integer.valueOf(ids[0]);
                    mate = 0;
                }else {
                    id = Integer.valueOf(ids[0]);
                    mate = Integer.valueOf(ids[1]);
                }
                Item item = Item.get(id,mate);
                item.setCount(data.count);
                if (!player.getInventory().canAddItem(item)) {
                    player.showFormWindow(PlayerBuyShop.getFoolPage(data.data, data.count));
                    this.buyStep.remove(player.getName());
                    return;
                }
                player.getInventory().addItem(item);
                Money.getInstance().reduceMoney(player,data.data.price*data.count);
                player.showFormWindow(PlayerBuyShop.getBuySuccessPage(data.data,data.getCount()));
            }
        }
    }
}
