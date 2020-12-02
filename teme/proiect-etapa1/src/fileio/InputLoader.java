package fileio;

import constants.Constants;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InputLoader {

    /**
     * The path of the input file
     */
    private final String inputPath;

    public InputLoader(String inputPath) {
        this.inputPath = inputPath;
    }

    public Input readData() {
        JSONParser jsonParser = new JSONParser();
        int numberOfTurns = 0;
        List<Consumer> consumers = new ArrayList<>();
        List<Distributor> distributors = new ArrayList<>();
        List<MonthlyUpdate> monthlyUpdates = new ArrayList<>();

        try {
            // Parsing the contents of the JSON file
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(inputPath));
            numberOfTurns = ((Long) (jsonObject).get(Constants.NUMBER_OF_TURNS))
                    .intValue();

            JSONObject jsonInitialData = (JSONObject) jsonObject.get(Constants.INITIAL_DATA);
            JSONArray jsonConsumers = (JSONArray) jsonInitialData.get(Constants.CONSUMERS);
            JSONArray jsonDistributors = (JSONArray) jsonInitialData.get(Constants.DISTRIBUTORS);

            JSONArray jsonMonthlyUpdates = (JSONArray) jsonObject.get(Constants.MONTHLY_UPDATES);

            if (jsonConsumers != null) {
                for (Object jsonConsumer : jsonConsumers) {
                    consumers.add(new Consumer(
                            ((Long) ((JSONObject) jsonConsumer).get(Constants.ID)).intValue(),
                            ((Long) ((JSONObject) jsonConsumer).get(Constants.INITIAL_BUDGET))
                                    .intValue(),
                            ((Long) ((JSONObject) jsonConsumer).get(Constants.MONTHLY_INCOME))
                                    .intValue()
                    ));
                }
            }

            if (jsonDistributors != null) {
                for (Object jsonDistributor : jsonDistributors) {
                    distributors.add(new Distributor(
                            ((Long) ((JSONObject) jsonDistributor).get(Constants.ID)).intValue(),
                            ((Long) ((JSONObject) jsonDistributor).get(Constants.CONTRACT_LENGTH))
                                    .intValue(),
                            ((Long) ((JSONObject) jsonDistributor).get(Constants.INITIAL_BUDGET))
                                    .intValue(),
                            ((Long) ((JSONObject) jsonDistributor)
                                    .get(Constants.INITIAL_INFRASTRUCTURE_COST)).intValue(),
                            ((Long) ((JSONObject) jsonDistributor)
                                    .get(Constants.INITIAL_PRODUCTION_COST)).intValue()
                    ));
                }
            }

            if (jsonMonthlyUpdates != null) {
                for (Object jsonMonthlyUpdate : jsonMonthlyUpdates) {
                    JSONArray jsonNewConsumers = (JSONArray) ((JSONObject)jsonMonthlyUpdate)
                            .get(Constants.NEW_CONSUMERS);
                    JSONArray jsonCostsChanges = (JSONArray) ((JSONObject)jsonMonthlyUpdate)
                            .get(Constants.COSTS_CHANGES);

                    List<Consumer> newConsumers = new ArrayList<>();
                    if (jsonNewConsumers != null) {
                        assert jsonConsumers != null;
                        for (Object jsonNewConsumer : jsonNewConsumers) {
                            newConsumers.add(new Consumer(
                                    ((Long) ((JSONObject) jsonNewConsumer).get(Constants.ID))
                                            .intValue(),
                                    ((Long) ((JSONObject) jsonNewConsumer)
                                            .get(Constants.INITIAL_BUDGET)).intValue(),
                                    ((Long) ((JSONObject) jsonNewConsumer)
                                            .get(Constants.MONTHLY_INCOME)).intValue()
                            ));
                        }
                    }

                    List<CostChange> costChanges = new ArrayList<>();
                    if (jsonCostsChanges != null) {
                        for (Object jsonCostChange : jsonCostsChanges) {
                            costChanges.add(new CostChange(
                                    ((Long) ((JSONObject) jsonCostChange).get(Constants.ID))
                                            .intValue(),
                                    ((Long) ((JSONObject) jsonCostChange)
                                            .get(Constants.INFRASTRUCTURE_COST)).intValue(),
                                    ((Long) ((JSONObject) jsonCostChange)
                                            .get(Constants.PRODUCTION_COST)).intValue()
                            ));
                        }
                    }

                    monthlyUpdates.add(new MonthlyUpdate(newConsumers, costChanges));
                }

            }

        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("PARSE EXCEPTION");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("FILE NOT FOUND");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO EXCEPTION");
        }

        return new Input(numberOfTurns, consumers, distributors, monthlyUpdates);
    }

}
