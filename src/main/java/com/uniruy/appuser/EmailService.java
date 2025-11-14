package com.uniruy.appuser;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    private final RestTemplate restTemplate = new RestTemplate();
    
    // SUA CHAVE DO RESEND - substitua pela sua
    private static final String RESEND_API_KEY = "re_EvptyiMR_MctkV6aHLRtnpRowcxr1pNQW";
    private static final String RESEND_URL = "https://api.resend.com/emails";
    private static final String FROM_EMAIL = "MedLink <contato.medlinksistemamedico@gmail.com>";
    // Ou use o domínio de teste do Resend: "onboarding@resend.dev"

    @Async
    public void enviarCodigoDeLogin(String emailDestino, String codigo) {
        String assunto = "MedLink - Sua Matrícula de Acesso";
        String texto = "Olá!\n\nObrigado por se registrar no MedLink.\n\nSeu código de acesso é: " + codigo
                + "\n\nUse este código para fazer login no sistema.\n\nAtenciosamente,\nEquipe MedLink";

        enviarEmailResend(emailDestino, assunto, texto);
    }

    @Async
    public void enviarCodigoRedefinicaoSenha(String emailDestino, String codigo) {
        String assunto = "MedLink - Código de Verificação";
        String texto = "Olá!\n\nVocê solicitou a redefinição de senha.\n\nSeu código de verificação é: " + codigo
                + "\n\nEste código é válido por 30 minutos.\n\nAtenciosamente,\nEquipe MedLink";

        enviarEmailResend(emailDestino, assunto, texto);
    }

    @Async
    public void sendEmail(String emailDestino, String assunto, String texto) {
        enviarEmailResend(emailDestino, assunto, texto);
    }

    private void enviarEmailResend(String emailDestino, String assunto, String texto) {
        try {
            System.out.println("=== TENTANDO ENVIAR EMAIL VIA RESEND ===");
            System.out.println("Para: " + emailDestino);
            System.out.println("Assunto: " + assunto);

            // Configurar headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + RESEND_API_KEY);
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Corpo da requisição
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("from", FROM_EMAIL);
            requestBody.put("to", new String[]{emailDestino});
            requestBody.put("subject", assunto);
            requestBody.put("text", texto);
            
            // Para HTML (opcional):
            // requestBody.put("html", "<strong>" + texto + "</strong>");

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            // Enviar requisição
            String response = restTemplate.postForObject(RESEND_URL, request, String.class);
            
            System.out.println("✅ RESPOSTA DO RESEND: " + response);
            System.out.println("✅ EMAIL ENVIADO COM SUCESSO!");
            
        } catch (Exception e) {
            System.err.println("❌ ERRO AO ENVIAR EMAIL VIA RESEND: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
