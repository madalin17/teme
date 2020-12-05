package fileio;

import constants.Constants;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class OutputLoader {

    private final String outputPath;

    private final List<Consumer> consumers;

    private final List<Distributor> distributors;

    public OutputLoader(final String outputPath, final List<Consumer> consumers,
                        final List<Distributor> distributors) {
        this.outputPath = outputPath;
        this.consumers = consumers;
        this.distributors = distributors;
    }

    @SuppressWarnings("unchecked")
    public void writeFile() {
        JSONObject obj = new JSONObject();

        JSONArray jsonConsumers = new JSONArray();
        consumers.forEach(c -> {
            JSONObject jsonConsumer = new JSONObject();
            jsonConsumer.put(Constants.ID, c.getId());
            jsonConsumer.put(Constants.IS_BANKRUPT, c.isBankrupt());
            jsonConsumer.put(Constants.BUDGET, c.getIncome());
            jsonConsumers.add(jsonConsumer);
        });
        obj.put(Constants.CONSUMERS, jsonConsumers);

        JSONArray jsonDistributors = new JSONArray();
        distributors.forEach(d -> {
            JSONObject jsonDistributor = new JSONObject();
            jsonDistributor.put(Constants.ID, d.getId());
            jsonDistributor.put(Constants.BUDGET, d.getBudget());
            jsonDistributor.put(Constants.IS_BANKRUPT, d.isBankrupt());

            JSONArray jsonCons = new JSONArray();
            d.getConsumers().forEach(c -> {
                JSONObject jsonConsumer = new JSONObject();
                jsonConsumer.put(Constants.CONSUMER_ID, c.getId());
                jsonConsumer.put(Constants.PRICE, c.getPrice());
                jsonConsumer.put(Constants.REMAINED_CONTRACT_MONTHS, c.getMonthsLeft());
                jsonCons.add(jsonConsumer);
            });
            jsonDistributor.put(Constants.CONTRACTS, jsonCons);

            jsonDistributors.add(jsonDistributor);
        });
        obj.put(Constants.DISTRIBUTORS, jsonDistributors);

        System.out.println(obj);

        try {
            FileWriter file = new FileWriter(outputPath);
            file.write(obj.toJSONString());
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

