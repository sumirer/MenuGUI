package sole.memory.menugui.email;


import cn.nukkit.Server;
import sole.memory.menugui.MenuGUI;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;


public class Email {

    public static final String MODIFY_PASSWORD = "password";
    public static final String ACCOUNT_CHECK = "check";

    private String player;

    private String verify;

    private String type;

    private String mailAccount;

    public Email(String player,String verify,String type,String mailAccount){
        this.player = player;
        this.mailAccount = mailAccount;
        this.verify = verify;
        this.type = type;
    }

    public void sendEmail(){
        sendEmail(false,"","","");
    }



    public boolean sendEmail(boolean privateEmail,String user,String title,String msg){
        try {
            String mail = MenuGUI.mail.getString("email", "790923880@qq.com");
            String password = MenuGUI.mail.getString("password", "aeueiysrjzfvbdbd");
            String mailType = MenuGUI.mail.getString("email_type", "qq");
            String port = MenuGUI.mail.getString("SSL_Port", "465");
            boolean ssl = MenuGUI.mail.getBoolean("SSL", true);
            boolean debug = MenuGUI.mail.getBoolean("DeBug", false);
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", "smtp");           // 使用的协议（JavaMail规范要求）
            props.setProperty("mail.smtp.host", "smtp." + mailType + ".com");   // 发件人的邮箱的 SMTP 服务器地址
            props.setProperty("mail.smtp.auth", "true");                    // 需要请求认证
            if (ssl) {
                props.setProperty("mail.smtp.port", port);
                props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.setProperty("mail.smtp.socketFactory.fallback", "false");
                props.setProperty("mail.smtp.socketFactory.port", port);
            }

            Session session = Session.getInstance(props);
            session.setDebug(debug);
            MimeMessage message;        // 设置为debug模式,可以查看详细的发送 log
            if (privateEmail){
                 message = createMessage(session, mail, mailAccount,title,user,msg);
            }else {
                 message = createMessage(session, mail, mailAccount, type);
            }
            Transport transport = session.getTransport();
            transport.connect(mail, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            return true;
        }catch (Exception e){
            Server.getInstance().getLogger().warning("MenuGUI mail send error,error info："+e.getMessage());
            return false;
        }
    }


    private MimeMessage createMessage(Session session, String sendMail, String receiveMail, String type) throws Exception {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(sendMail, "MenuGUI", "UTF-8"));
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, "SoleMemory", "UTF-8"));
        if (type.equals(Email.MODIFY_PASSWORD)) {
            message.setSubject("MenuGUI密码修改验证码", "UTF-8");
            message.setContent("User: "+player+"  You're modifying your password, verification code is: "+verify, "text/html;charset=UTF-8");
        }
        if (type.equals(Email.ACCOUNT_CHECK)){
            message.setSubject("MenuGUI邮箱绑定验证", "UTF-8");
            message.setContent("User: "+player+"  You are binding you mailbox, verification code is: "+verify, "text/html;charset=UTF-8");
        }
        message.setSentDate(new Date());
        message.saveChanges();
        return message;
    }

    private MimeMessage createMessage( Session session, String sendMail, String receiveMail,String title,String user,String msg) throws Exception {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(sendMail, user, "UTF-8"));
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, "SoleMemory", "UTF-8"));
        message.setSubject(title, "UTF-8");
        message.setContent(msg, "text/html;charset=UTF-8");
        message.setSentDate(new Date());
        message.saveChanges();
        return message;
    }
}
