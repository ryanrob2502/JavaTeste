package com.uniruy.appuser;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    private final RestTemplate restTemplate = new RestTemplate();
    
    // SUAS CREDENCIAIS - SUBSTITUA COM AS SUAS
    private static final String EMAILJS_SERVICE_ID = "service_950grm9";
    private static final String EMAILJS_TEMPLATE_ID = "template_0rfxsih";
    private static final String EMAILJS_PUBLIC_KEY = "HtFNur5Q7vzDYN0TH";
    private static final String EMAILJS_USER_ID = "HtFNur5Q7vzDYN0TH";
    private static final String EMAILJS_URL = "https://api.emailjs.com/api/v1.0/email/send";

    @Async
    public void enviarCodigoDeLogin(String emailDestino, String codigo) {
        enviarEmailEmailJS(emailDestino, "MedLink - Sua Matrícula de Acesso", 
            "Olá!\\n\\nObrigado por se registrar no MedLink.\\n\\nSeu código de acesso é: " + codigo +
            "\\n\\nUse este código para fazer login no sistema.\\n\\nAtenciosamente,\\nEquipe MedLink", codigo);
    }

    @Async
    public void enviarCodigoRedefinicaoSenha(String emailDestino, String codigo) {
        enviarEmailEmailJS(emailDestino, "MedLink - Código de Verificação", 
            "Olá!\\n\\nVocê solicitou a redefinição de senha.\\n\\nSeu código de verificação é: " + codigo +
            "\\n\\nEste código é válido por 30 minutos.\\n\\nAtenciosamente,\\nEquipe MedLink", codigo);
    }

    @Async
    public void sendEmail(String emailDestino, String assunto, String texto) {
        enviarEmailEmailJS(emailDestino, assunto, texto, "123456");
    }

    private void enviarEmailEmailJS(String emailDestino, String assunto, String texto, String codigo) {
        try {
            System.out.println("=== TENTANDO ENVIAR EMAIL VIA EMAILJS ===");
            System.out.println("Para: " + emailDestino);
            System.out.println("Assunto: " + assunto);
            System.out.println("Código: " + codigo);

            // Preparar o corpo da requisição
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("service_id", EMAILJS_SERVICE_ID);
            requestBody.put("template_id", EMAILJS_TEMPLATE_ID);
            requestBody.put("user_id", EMAILJS_USER_ID);
            
            // Parâmetros do template
            Map<String, String> templateParams = new HashMap<>();
            templateParams.put("to_email", emailDestino);
            templateParams.put("subject", assunto);
            templateParams.put("message", texto);
            templateParams.put("codigo", codigo);
            templateParams.put("from_name", "MedLink Sistema");
            
            requestBody.put("template_params", templateParams);
            requestBody.put("accessToken", EMAILJS_PUBLIC_KEY);

            System.out.println("Enviando requisição para EmailJS...");
            
            // Fazer a requisição POST
            String response = restTemplate.postForObject(EMAILJS_URL, requestBody, String.class);
            
            System.out.println("✅ RESPOSTA DO EMAILJS: " + response);
            System.out.println("✅ EMAIL ENVIADO COM SUCESSO!");
            
        } catch (Exception e) {
            System.err.println("❌ ERRO AO ENVIAR EMAIL: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
