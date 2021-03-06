package com.example.demo.services;

import com.example.demo.models.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoronaVirusDataService {

    private static String VIRUS_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    private List<LocationStats> allStats = new ArrayList<>();

    @PostConstruct
    @Scheduled(cron = "* 1 * * * *")
    public void fetchVirusData() throws IOException, InterruptedException {
        List<LocationStats> newStats = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(VIRUS_URL)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        System.out.println(response.body());

        StringReader reader = new StringReader(response.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
        for (CSVRecord record : records) {
            LocationStats locationStat = new LocationStats();
//            state = record.get("Province/State");
//            System.out.println(state);
            locationStat.setState(record.get("Province/State"));
            locationStat.setCountry(record.get("Country/Region"));
            locationStat.setLatestTotalCases(Integer.parseInt(record.get(record.size() - 1)));
//            System.out.println(locationStat.toString());
            int delta = Integer.parseInt(record.get(record.size() - 1)) - Integer.parseInt(record.get(record.size() - 2));
            locationStat.setDelta(delta);
            double aFrac = Double.parseDouble(record.get(record.size() - 1)) - Double.parseDouble(record.get(record.size() - 2));
            double bFrac = Double.parseDouble(record.get(record.size() - 2)) - Double.parseDouble(record.get(record.size() - 3));
//            locationStat.setIncProportion(aFrac / bFrac);
            double propTotal = 0;
            if (bFrac != 0) {
                propTotal = aFrac / bFrac;
                for (int i = 2; i < 6; i++) {
                    aFrac = Double.parseDouble(record.get(record.size() - i)) - Double.parseDouble(record.get(record.size() - (i + 1)));
                    bFrac = Double.parseDouble(record.get(record.size() - (i + 1))) - Double.parseDouble(record.get(record.size() - (i + 2)));
                    if (bFrac == 0) {
                        propTotal = 0;
                        break;
                    }
                    propTotal += aFrac / bFrac;
                }
                propTotal /= 5;
            }
            locationStat.setIncProportion(propTotal);
            String toShow = String.format("%.2f", propTotal);
            if(propTotal == 0) {
                toShow = "N/A";
            }
            locationStat.setIncShow(toShow);
            newStats.add(locationStat);
//            String customerNo = record.get("CustomerNo");
//            String name = record.get("Name");
        }
        this.allStats = newStats;
    }

    public List<LocationStats> getAllStats() {
        return allStats;
    }
}
