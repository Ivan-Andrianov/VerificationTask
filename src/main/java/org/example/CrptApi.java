package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class CrptApi {

    private final static String url = "https://ismp.crpt.ru/api/v3/lk/documents/create";
    private final int requestLimit;
    private int requestCount; //Переменная, контролирующая количество потоков.

    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        this.requestLimit = requestLimit;
        this.requestCount = 0;
        Timer timer = new Timer();
        timer.schedule(new UpdateRequestCountTimer(),timeUnit.toMillis(1));
    }

    public void createDocument(Document document, String sign){
        new SendCreateDocumentRequestThread(sign, document);
    }

    public String documentToJson(Document document){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(document);
            return json;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка при преобразовании документа в JSON формат.");
        }
    }

    public void sendCreateDocumentRequest(String jsonDocument){
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            BufferedWriter requestBodyWriter = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            requestBodyWriter.write(jsonDocument);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при отправке запроса.");
        }finally {
            if (connection!=null) {
                connection.disconnect();
            }
        }
    }

    //Поток-таймер, обновляющий переменную requestCount каждую единицу времени
    class UpdateRequestCountTimer extends TimerTask{
        @Override
        public void run() {
            synchronized (CrptApi.this) {
                requestCount = 0;
                CrptApi.this.notifyAll();
            }
        }
    }

    //Поток, создающий запрос на создание документа
    class SendCreateDocumentRequestThread extends Thread{
        public String sign;
        public Document document;

        public SendCreateDocumentRequestThread(String sign,Document document){
            this.sign = sign;
            this.document = document;
            start();
        }

        @Override
        public void run() {
            synchronized (CrptApi.this) {
                while (requestCount == requestLimit) {
                    try {
                        CrptApi.this.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                requestCount++;
                System.out.println(requestCount);
                CrptApi.this.notifyAll();
            }
            document.sign = sign;
            sendCreateDocumentRequest(documentToJson(document));
        }
    }

    public class Document{
        public DocumentDescription documentDescription;
        public String doc_id;
        public String doc_status;
        public String docType;
        public boolean importRequest;
        public String owner_inn;
        public String participant_inn;
        public String producer_inn;
        public String production_date;
        public String production_type;
        public List<Product> products;
        public String reg_date;
        public String reg_number;
        public String sign;

        public Document(DocumentDescription documentDescription, String doc_id, String doc_status, String docType, boolean importRequest, String owner_inn, String participant_inn, String producer_inn, String production_date, String production_type,List<Product> products, String reg_date, String reg_number) {
            this.documentDescription = documentDescription;
            this.doc_id = doc_id;
            this.doc_status = doc_status;
            this.docType = docType;
            this.importRequest = importRequest;
            this.owner_inn = owner_inn;
            this.participant_inn = participant_inn;
            this.producer_inn = producer_inn;
            this.production_date = production_date;
            this.production_type = production_type;
            this.products = products;
            this.reg_date = reg_date;
            this.reg_number =reg_number;
        }

    }

    public class DocumentDescription{

        public String participantInn;

        DocumentDescription(String participantInn){
            this.participantInn = participantInn;
        }

    }

    public class Product {
        public String certificate_document;
        public String certificate_document_date;
        public String owner_inn;
        public String producer_inn;
        public String production_date;
        public String tnved_code;
        public String uit_code;
        public String uitu_code;

        public Product(String certificate_document, String certificate_document_date, String owner_inn, String producer_inn, String production_date, String tnved_code, String uit_code, String uitu_code) {
            this.certificate_document = certificate_document;
            this.certificate_document_date = certificate_document_date;
            this.owner_inn = owner_inn;
            this.producer_inn = producer_inn;
            this.production_date = production_date;
            this.tnved_code = tnved_code;
            this.uit_code = uit_code;
            this.uitu_code = uitu_code;
        }
    }
}
