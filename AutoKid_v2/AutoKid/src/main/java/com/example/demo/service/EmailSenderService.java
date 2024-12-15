package com.example.demo.service;

import jakarta.mail.internet.MimeMessage;
import org.bouncycastle.util.encoders.UTF8;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;

    private void sendEmail(String to, String subject, String text) {
        try {
            // Tạo email
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text, true);
            message.setFrom("tungsoi2462003@gmail.com"); // Email người gửi (tùy chọn)

            // Gửi email
            mailSender.send(mimeMessage);
            System.out.println("Email đã được gửi thành công đến " + to);
        } catch (Exception e) {
            System.err.println("Gửi email thất bại: " + e.getMessage());
        }
    }

    public void sendMailToKH(String emailKH, String maHD, String tenNN) {
        String toEmail = emailKH;
        String subject = "[AUTOKID] THƯ THÔNG BÁO ĐẶT HÀNG THÀNH CÔNG";
        String text = "Xin chào anh/chị " + tenNN + "<br/>" +
                "Quý khách vừa mới đặt hàng thành công tại shop bán xe đạp AutoKid. " +
                "Để tra cứu thông tin của đơn hàng quý khách vui lòng thực hiện các bước sau:<br/> " +
                "   B1: Truy cập vào website của AutoKid <a href='http://localhost:8080/autokid/home'>Tại đây<a>.<br/>" +
                "   B2: Chọn mục 'Tra cứu đơn' trên menu.<br/>" +
                "   B3: Nhập mã đơn hàng, sau đó ấn 'Tra cứu'.<br/>" +
                "Mã đơn hàng của quý khách là: <strong>" + maHD + "</strong><br/>" +
                "Cảm ơn quý khách đã tin dùng sản phẩm của AutoKid. Mọi thắc mắc, cần hỗ trợ vui lòng liên hệ đến số điện thoại: <strong>1900 5678</strong><br/>" +
                "Thân mến.";
        sendEmail(toEmail, subject, text);
    }
}
