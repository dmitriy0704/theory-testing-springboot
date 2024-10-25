package dev.folomkin.mockito.mock_annotation_demo;

public class DataProcessor {

    private DataService dataService;

    public DataProcessor(DataService dataService) {
        this.dataService = dataService;
    }

    public int process() {
        return dataService.retrieveData() * 2;
    }

}
