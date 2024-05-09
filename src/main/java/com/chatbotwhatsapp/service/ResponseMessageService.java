package com.chatbotwhatsapp.service;

import com.chatbotwhatsapp.model.DialogflowMessage;
import com.chatbotwhatsapp.model.TypeWhatsAppMessage;
import com.chatbotwhatsapp.model.whatsapp.responseMessage.Message;
import com.chatbotwhatsapp.model.whatsapp.responseMessage.ResponseMessage;
import com.chatbotwhatsapp.persistence.crud.UserRepository;
import com.chatbotwhatsapp.persistence.entity.User;
import com.chatbotwhatsapp.util.ContentType;
import com.chatbotwhatsapp.util.FileType;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ResponseMessageService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DialogFlowService dialogFlowService;
    @Value("${whatsapp.webhook.url}")
    private String whatsappWebhookURL;
    @Value("${whatsapp.api.token}")
    private String bearerToken;
    private final RestTemplate restTemplate = new RestTemplate();
    private final Gson gson = new Gson();
    private final Timer timer = new Timer();
    private final Pattern pattern = Pattern.compile("\\.(\\w+)$");


    public boolean sendRequestMessageToWhatsApp(String phoneNumber, TypeWhatsAppMessage typeMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + bearerToken);
        String messageRequest = buildJson(phoneNumber, typeMessage);
        HttpEntity<String> entity = new HttpEntity<>(messageRequest, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(whatsappWebhookURL, entity, String.class);
        return responseEntity.getStatusCode().is2xxSuccessful();
    }

    public String sendMessageProcessed(String messageJson) {
        ResponseMessage responseMessage = gson.fromJson(messageJson, ResponseMessage.class);
        List<Message> listMessages = getMessages(responseMessage);
        if (listMessages != null && !listMessages.isEmpty()) {
            String phoneNumber = getPhoneNumberFromMessage(listMessages);
            savePhoneNumberIfNotExists(phoneNumber, responseMessage);
            String messageBody = getMessage(listMessages);
            Queue<DialogflowMessage> queueResponse = dialogFlowService.getResponseMessageProcessed(messageBody);
            sendMessageSequentially(queueResponse, phoneNumber);

        }
        return "Something went wrong";
    }

    private void sendMessageSequentially(Queue<DialogflowMessage> queue, String phoneNumber) {
        if (!queue.isEmpty()) {
            DialogflowMessage response = queue.poll();
            if (!response.content().isEmpty()) {
                boolean successMessage = sendWhatsAppMessage(response, phoneNumber);
                if (successMessage) {
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            sendMessageSequentially(queue, phoneNumber);
                        }
                    }, 2000);
                }
            }

        }

    }

    private boolean sendWhatsAppMessage(DialogflowMessage response, String phoneNumber) {
        TypeWhatsAppMessage messageType;
        if (response.type().equals(ContentType.URL)) messageType = getFileTypeFromUrl(response.content());
        else {
            JsonObject textMessage = new JsonObject();
            textMessage.addProperty("body", response.content());
            messageType = new TypeWhatsAppMessage(FileType.TEXT.toString(), textMessage);

        }
        return sendRequestMessageToWhatsApp(phoneNumber, messageType);

    }

    private void savePhoneNumberIfNotExists(String phoneNumber, ResponseMessage responseMessage) {
        if (!existPhoneNumber(phoneNumber)) {
            String name = getNameFromResponse(responseMessage);
            userRepository.save(new User(phoneNumber, name));
        }
    }

    public TypeWhatsAppMessage getFileTypeFromUrl(String url) {
        Matcher matcher = pattern.matcher(url);
        JsonObject typeMessage = new JsonObject();

        if (!matcher.find()) {
            typeMessage.addProperty("preview_url", true);
            typeMessage.addProperty("body", url);
            return new TypeWhatsAppMessage(FileType.TEXT.toString(), typeMessage);
        }
        String extension = matcher.group(1).toLowerCase();
        typeMessage.addProperty("link", url);
        return switch (extension) {
            case "mp4", "3gp" -> new TypeWhatsAppMessage(FileType.VIDEO.toString(), typeMessage);
            case "mp3", "aac", "amr", "mp4a", "ogg" -> new TypeWhatsAppMessage(FileType.AUDIO.toString(), typeMessage);
            case "txt", "doc", "docx", "pdf", "xls", "xlsx", "ppt", "pptx" ->
                    new TypeWhatsAppMessage(FileType.DOCUMENT.toString(), typeMessage);
            case "webp", "jpeg", "png", "jpg" -> new TypeWhatsAppMessage(FileType.IMAGE.toString(), typeMessage);
            default -> {
                typeMessage.remove("link");
                typeMessage.addProperty("body", url);
                yield new TypeWhatsAppMessage(FileType.TEXT.toString(), typeMessage);
            }
        };
    }

    private List<Message> getMessages(ResponseMessage responseMessage) {
        return responseMessage.getEntry().get(0).getChanges().get(0).getValue().getMessages();
    }

    private String getNameFromResponse(ResponseMessage responseMessage) {
        return responseMessage.getEntry().get(0).getChanges().get(0).getValue().getContacts().get(0).getProfile().getName();
    }

    private String getMessage(List<Message> messages) {
        return messages.get(0).getText().getBody();
    }

    private String getPhoneNumberFromMessage(List<Message> messages) {
        return messages.get(0).getFrom();
    }

    private boolean existPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).isPresent();
    }

    private String buildJson(String phoneNumber, TypeWhatsAppMessage typeMessage) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("messaging_product", "whatsapp");
        jsonObject.addProperty("recipient_type", "individual");
        jsonObject.addProperty("to", phoneNumber);
        jsonObject.addProperty("type", typeMessage.type());
        jsonObject.add(typeMessage.type(), typeMessage.content());

        return gson.toJson(jsonObject);
    }

}
