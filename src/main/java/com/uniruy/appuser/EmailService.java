package com.uniruy.appuser;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    @Autowired
    private SendGrid sendGrid;

    private static final String EMAIL_ORIGEM = "contato.medlinksistemamedico@gmail.com";

    @Async
    public void enviarCodigoDeLogin(String emailDestino, String codigo) {
        String assunto = "MedLink - Sua matricula de Acesso";
        String texto = "Olá!\n\nObrigado por se registrar no MedLink.\n\nSeu código de acesso é: " + codigo
                + "\n\nUse este código para fazer login no sistema.\n\nAtenciosamente,\nEquipe MedLink";

        enviarEmail(emailDestino, assunto, texto);
    }

    @Async
    public void enviarCodigoRedefinicaoSenha(String emailDestino, String codigo) {
        String assunto = "MedLink - Código de Verificação";
        String texto = "Olá!\n\nVocê solicitou a redefinição de senha.\n\nSeu código de verificação é: " + codigo
                + "\n\nEste código é válido por 30 minutos.\n\nSe você não solicitou esta redefinição, ignore este e-mail."
                + "\n\nAtenciosamente,\nEquipe MedLink";

        enviarEmail(emailDestino, assunto, texto);
    }

    @Async
    public void enviarNovaSenha(String emailDestino, String novaSenha) {
        String assunto = "MedLink - Sua Nova Senha Temporária";
        String texto = "Olá!\n\nVocê solicitou uma nova senha.\n\nSua nova senha temporária é: " + novaSenha
                + "\n\nPor favor, use-a para fazer login e altere sua senha imediatamente."
                + "\n\nAtenciosamente,\nEquipe MedLink";

        enviarEmail(emailDestino, assunto, texto);
    }

    // MÉTODO ADICIONADO PARA USAR NO PageController
    @Async
    public void sendEmail(String emailDestino, String assunto, String texto) {
        enviarEmail(emailDestino, assunto, texto);
    }

    private void enviarEmail(String emailDestino, String assunto, String texto) {
        try {
            System.out.println("Tentando enviar email para: " + emailDestino);
            System.out.println("Assunto: " + assunto);
            
            Email from = new Email(EMAIL_ORIGEM);
            Email to = new Email(emailDestino);
            Content content = new Content("text/plain", texto);

            Mail mail = new Mail(from, assunto, to, content);

            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            // Adicione logs da resposta
            Response response = sendGrid.api(request);
            System.out.println("Status Code: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());
            System.out.println("Response Headers: " + response.getHeaders());

            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                System.out.println("Email enviado com sucesso!");
            } else {
                System.out.println("Falha ao enviar email. Status: " + response.getStatusCode());
            }

        } catch (IOException e) {
            System.err.println("Erro IOException ao enviar email: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro geral ao enviar email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
