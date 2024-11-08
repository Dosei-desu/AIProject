package com.example.aiproject.service;

import com.example.aiproject.dto.ChatCompletionRequest;
import com.example.aiproject.dto.ChatCompletionResponse;
import com.example.aiproject.dto.MyResponse;
import com.example.aiproject.dto.Treatment;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Service
public class OpenAiService {

    public static final Logger logger = LoggerFactory.getLogger(OpenAiService.class);

    @Value("${app.api-key}")
    private String API_KEY;

    //See here for a decent explanation of the parameters send to the API via the requestBody
    //https://platform.openai.com/docs/api-reference/completions/create

    @Value("${app.url}")
    public String URL;

    @Value("${app.model}")
    public String MODEL;

    @Value("${app.temperature}")
    public double TEMPERATURE;

    @Value("${app.max_tokens}")
    public int MAX_TOKENS;

    @Value("${app.frequency_penalty}")
    public double FREQUENCY_PENALTY;

    @Value("${app.presence_penalty}")
    public double PRESENCE_PENALTY;

    @Value("${app.top_p}")
    public double TOP_P;

    private WebClient client;

    public OpenAiService() {
        this.client = WebClient.create();
    }
    //Use this constructor for testing, to inject a mock client
    public OpenAiService(WebClient client) {
        this.client = client;
    }

    public List<Treatment> generateTreatmentList(String prompt, String primingMessage) {
        //calls the MakeRequest method, takes the reply and splits it based on symbols
        //that were planted in the priming message (% and #)
        //then it places those in a list of treatments
        MyResponse response = makeRequest(prompt,primingMessage);
        System.out.println(response.getAnswer());
        if (response.getAnswer().toLowerCase().contains("try again"))
        {
            return List.of(new Treatment("Try Again",
                    "Please enter a valid symptom."));
        }
        else
        {
            List<Treatment> treatmentList = new ArrayList<>();
            String[] responseList = response.getAnswer().split("#");
            for (String s : responseList) {
                String[] splitList = s.split("%");
                //uses trim, since the OpenAI response seems to contain a lot of empty spaces
                treatmentList.add(new Treatment(
                        splitList[0].trim(),
                        splitList[1].trim()
                ));
            }
            return treatmentList;
        }
    }

    public MyResponse makeRequest(String userPrompt, String primingMessage) {

        ChatCompletionRequest requestDto = new ChatCompletionRequest();
        requestDto.setModel(MODEL);
        requestDto.setTemperature(TEMPERATURE);
        requestDto.setMax_tokens(MAX_TOKENS);
        requestDto.setTop_p(TOP_P);
        requestDto.setFrequency_penalty(FREQUENCY_PENALTY);
        requestDto.setPresence_penalty(PRESENCE_PENALTY);
        requestDto.getMessages().add(new ChatCompletionRequest.Message("system", primingMessage));
        requestDto.getMessages().add(new ChatCompletionRequest.Message("user", userPrompt));

        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        String err =  null;
        try {
            json = mapper.writeValueAsString(requestDto);
            System.out.println(json);
            ChatCompletionResponse response = client.post()
                    .uri(new URI(URL))
                    .header("Authorization", "Bearer " + API_KEY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(json))
                    .retrieve()
                    .bodyToMono(ChatCompletionResponse.class)
                    .block();
            String responseMsg = response.getChoices().get(0).getMessage().getContent();
            int tokensUsed = response.getUsage().getTotal_tokens();
            System.out.print("Tokens used: " + tokensUsed);
            System.out.print(". Cost ($0.0015 / 1K tokens) : $" + String.format("%6f",(tokensUsed * 0.0015 / 1000)));
            System.out.println(". For 1$, this is the amount of similar requests you can make: " + Math.round(1/(tokensUsed * 0.0015 / 1000)));
            return new MyResponse(responseMsg);
        }
        catch (WebClientResponseException e){
            //This is how you can get the status code and message reported back by the remote API
            logger.error("Error response status code: " + e.getRawStatusCode());
            logger.error("Error response body: " + e.getResponseBodyAsString());
            logger.error("WebClientResponseException", e);
            err = "Internal Server Error, due to a failed request to external service. You could try again" +
                    "( While you develop, make sure to consult the detailed error message on your backend)";
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, err);
        }
        catch (Exception e) {
            logger.error("Exception", e);
            err = "Internal Server Error - You could try again" +
                    "( While you develop, make sure to consult the detailed error message on your backend)";
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, err);
        }
    }
}
