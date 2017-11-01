package sole.memory.guishop.shop;

import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowModal;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.utils.TextFormat;

/**
 * @author SoleMeory
 * on 2017/10/31.
 */
public class AdminSetShop {

    //TODO: del model
    private String model = "add";

    public float price = 0;

    public String name = "";

    public String id = "";



    public boolean isCheckData(){
        return price!=0 && !name.equals("") && model.equals("add");
    }

    public static FormWindowCustom getSearchPage(){
        FormWindowCustom custom = new FormWindowCustom("请输入要添加物品的ID");
        ElementLabel label = new ElementLabel(TextFormat.GOLD+"请输入要添加物品的ID\n 格式为(ID:特殊值(无特殊值不需要:))");
        custom.addElement(label);
        ElementInput input = new ElementInput("请输入ID",TextFormat.DARK_GRAY+"(ID:特殊值(无特殊值不需要:)");
        custom.addElement(input);
        return custom;
    }



    public FormWindowCustom getInputPricePage(){
        FormWindowCustom custom = new FormWindowCustom("请输入要添加物品的价格");
        ElementLabel label = new ElementLabel(TextFormat.AQUA+"你正在添加物品： "+name+"\n"+TextFormat.GOLD+"请输入要添加物品的ID\n 格式为(price:int)");
        custom.addElement(label);
        ElementInput input = new ElementInput("请输入价格",TextFormat.DARK_GRAY+"(价格:数字)");
        custom.addElement(input);
        return custom;
    }

    public FormWindowModal getCheckPage(){
        String info = TextFormat.GOLD+"物品名字: "+name+"\n"+TextFormat.AQUA+"单价: "+price+"\n\n\n"+TextFormat.GREEN+"请确认信息...";
        return new FormWindowModal("请确认信息",info,"确认","取消");
    }

    public FormWindowSimple getSuccessPage(){
        return new FormWindowSimple("添加成功","成功添加物品到商店，请关闭此界面");
    }

    public FormWindowSimple getFailedPage(){
        return new FormWindowSimple("添加失败",TextFormat.RED+"添加物品到商店失败，请关闭此界面，并检查输入数据是否正确");
    }
}
