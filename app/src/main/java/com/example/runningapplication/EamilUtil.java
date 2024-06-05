package com.example.runningapplication;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/*-------------------------------------------------------------------------
 * 作者：
 * 创建时间： 2019/5/22
 * 版本号：v1.0
 * 本类主要用途描述：
 *
 *-------------------------------------------------------------------------*/
public class EamilUtil {
    public static String account = "512292589@qq.com";//自己的邮箱
    public static String password = "svhddaqcpjlvbihi";//密码


    // 发件人邮箱的 SMTP 服务器地址, 必须准确, 不同邮件服务器地址不同, 一般(只是一般, 绝非绝对)格式为: smtp.xxx.com
// 网易163邮箱的 SMTP 服务器地址为: smtp.163.com
    public static String myEmailSMTPHost = "smtp.qq.com";

    /**
     * 发送邮件的方法
     *
     * @param to   邮件的接收方
     * @param code 邮件的激活码
     */
    public static void sendMail(String to, String code) {
// 1.创建连接对象，链接到邮箱服务器
        Properties props = new Properties(); // 参数配置
        props.setProperty("mail.transport.protocol", "smtp");// 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", myEmailSMTPHost);// 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true"); // 需要请求认证
        props.setProperty("mail.smtp.starttls.enable","true");
        props.setProperty("mail.smtp.starttls.required","true");
        props.setProperty("mail.smtp.port","465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.ssl.protocols","TLSv1.2");



// 2.根据配置创建会话对象, 用于和邮件服务器交互
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(account, password);
            }
        });


        try {
// 3.创建邮件对象
            Message message = new MimeMessage(session);
// 3.1设置发件人
            message.setFrom(new InternetAddress(account));
// 3.2设置收件人
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
// 3.3设置邮件的主题
            message.setSubject("");
// 3.4设置邮件的正文
//message.setContent("<h1>来自智慧电梯的激活邮件，您的验证码是：</h1><h3><a href='http://localhost:10080/Demo_JavaMail/active?code=" + code + "'>http://localhost:10080/Demo_JavaMail/active?code=" + code + "</h3>", "text/html;charset=UTF-8");
            message.setContent("<h1>，您的验证码是：" + code, "text/html;charset=UTF-8");
// 4.发送邮件
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}