package org.example;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        CrptApi api = new CrptApi(TimeUnit.MINUTES,5);
        CrptApi.DocumentDescription documentDescription = api.new DocumentDescription("description");
        List<CrptApi.Product> list = new ArrayList<>();
        list.add(api.new Product("document","10.02.2020","owner_inn","producer_inn","10.02.2020","tvned_code","uit_code","uitu_code"));
        list.add(api.new Product("document2","11.02.2020","owner_inn2","producer_inn2","11.02.2020","tvned_code2","uit_code2","uitu_code2"));
        CrptApi.Document document = api.new Document(documentDescription,"doc_id","doc_status","doc_type",true,"owner_inn","participant_inn","producer_inn","10.02.2020","production_type",list,"10.02.2020","reg_number");
        CrptApi.Document document2 = api.new Document(documentDescription,"doc_id","doc_status","doc_type",true,"owner_inn","participant_inn","producer_inn","10.02.2020","production_type",list,"10.02.2020","reg_number");
        CrptApi.Document document3 = api.new Document(documentDescription,"doc_id","doc_status","doc_type",true,"owner_inn","participant_inn","producer_inn","10.02.2020","production_type",list,"10.02.2020","reg_number");
        CrptApi.Document document4 = api.new Document(documentDescription,"doc_id","doc_status","doc_type",true,"owner_inn","participant_inn","producer_inn","10.02.2020","production_type",list,"10.02.2020","reg_number");
        CrptApi.Document document5 = api.new Document(documentDescription,"doc_id","doc_status","doc_type",true,"owner_inn","participant_inn","producer_inn","10.02.2020","production_type",list,"10.02.2020","reg_number");
        CrptApi.Document document6 = api.new Document(documentDescription,"doc_id","doc_status","doc_type",true,"owner_inn","participant_inn","producer_inn","10.02.2020","production_type",list,"10.02.2020","reg_number");
        CrptApi.Document document7 = api.new Document(documentDescription,"doc_id","doc_status","doc_type",true,"owner_inn","participant_inn","producer_inn","10.02.2020","production_type",list,"10.02.2020","reg_number");
        api.createDocument(document,"Ivan");
        api.createDocument(document2,"Ivan");
        api.createDocument(document3,"Ivan");
        api.createDocument(document4,"Ivan");
        api.createDocument(document5,"Ivan");
        api.createDocument(document6,"Ivan");
        api.createDocument(document7,"Ivan");
    }
}
