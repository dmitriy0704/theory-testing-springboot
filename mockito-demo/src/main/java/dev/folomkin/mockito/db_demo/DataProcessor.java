package dev.folomkin.mockito.db_demo;

public class DataProcessor {

    final DataService dataService;

    public DataProcessor(DataService dataService) {
        this.dataService = dataService;
    }

    public double processData() {
        return dataService.retrieveData() * 2;
    }

}
