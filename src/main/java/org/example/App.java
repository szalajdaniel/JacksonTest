package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.model.RobotReport;

import java.util.List;


//====================Cześć1==================
// PYTANIE1:
//Java w trakcie działania zapomina ze to miala byc lista dronow, wiec jackson tworzy liste zwyklych map co skutkuje
//bledem przy odczycie, zeby jackson wiedzial jakiego oibiektu ma uzyc musi uzyc new TypeReference<List<Drone>>...
//PYTANIE2:
//files.lines() otwiera fizyczne połączenia z plikiem na dysku, ktore trzeba zamknac samemu uzywajac (try-with-resources).
//Zwykłe strumienie z list żyją tylko w pamięci RAM i czyszczą się same i nie trzeba ich zamykac

public class App {
    public static void main(String[] args) throws JsonProcessingException {
        System.out.println("====================Cześć2==================");
        String orderJson = """
                        {
                          "task_id": "TSK-882",
                          "urgent": true,
                          "payload": {
                            "items": [
                              {"id": 101, "weight": 2.5},
                              {"id": 102, "weight": 4.1}
                            ],
                            "destination": "Sektor A"
                          }
                        }
                """;
        ObjectMapper dynamicMapper = new ObjectMapper();
        JsonNode rootNode = dynamicMapper.readTree(orderJson);

        boolean isUrgent = rootNode.path("urgent").asBoolean();
        String taskId = rootNode.path("task_id").asText();

        if (isUrgent) {
            System.out.println("Zlecenie " + taskId + " jest pilne!");
        }
        JsonNode itemsNode = rootNode.path("payload").path("items");
        if (itemsNode.isArray()) {
            double totalWeight = 0;
            for (JsonNode item : itemsNode) {
                totalWeight += item.path("weight").asDouble();
            }
            System.out.println("Łączna waga paczek: " + totalWeight);
        } else System.out.println("BraK paczek w zleceniu");

        System.out.println("====================Cześć3==================");

        String reportsJson = """
                [
                  {
                    "serialNumber": "RBT-01",
                    "commissionDate": "15-11-2025",
                    "battery": 88
                  },
                  {
                    "serialNumber": "RBT-02",
                    "commissionDate": "20-11-2025",
                    "battery": 45,
                    "needsMaintenance": true
                  }
                ]
                """;
        ObjectMapper configuredMapper = new ObjectMapper();
        configuredMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        configuredMapper.registerModule(new JavaTimeModule());
        List<RobotReport> reports = configuredMapper.readValue(reportsJson, new TypeReference<List<RobotReport>>() {
        });
        for (RobotReport report : reports) {
            System.out.println("Robot: " + report.serialNumber() + ", Data wdrożenia: " + report.commissionDate());
        }
    }
//
}
