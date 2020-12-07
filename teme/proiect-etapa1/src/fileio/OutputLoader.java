package fileio;

import com.fasterxml.jackson.databind.ObjectMapper;
import constants.Constants;
import energy.Consumer;
import energy.Distributor;
import org.json.simple.JSONArray;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;

public final class OutputLoader {

    private final String outputPath;

    private final List<Consumer> consumers;

    private final List<Distributor> distributors;

    public OutputLoader(final String outputPath, final List<Consumer> consumers,
                        final List<Distributor> distributors) {
        this.outputPath = outputPath;
        this.consumers = consumers;
        this.distributors = distributors;
    }

    /**
     * Function takes the path to an output file and writes all important data
     * for all consumers and distributors in order
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void writeFile() {
        LinkedHashMap obj = new LinkedHashMap();

        JSONArray jsonConsumers = new JSONArray();
        consumers.forEach(c -> {
            LinkedHashMap jsonConsumer = new LinkedHashMap();
            jsonConsumer.put(Constants.ID, c.getId());
            jsonConsumer.put(Constants.IS_BANKRUPT, c.isBankrupt());
            jsonConsumer.put(Constants.BUDGET, c.getIncome());
            jsonConsumers.add(jsonConsumer);
        });
        obj.put(Constants.CONSUMERS, jsonConsumers);

        JSONArray jsonDistributors = new JSONArray();
        distributors.forEach(d -> {
            LinkedHashMap jsonDistributor = new LinkedHashMap();
            jsonDistributor.put(Constants.ID, d.getId());
            jsonDistributor.put(Constants.BUDGET, d.getBudget());
            jsonDistributor.put(Constants.IS_BANKRUPT, d.isBankrupt());

            JSONArray jsonCons = new JSONArray();
            d.getConsumers().forEach((key, value) -> {
                LinkedHashMap jsonConsumer = new LinkedHashMap();
                jsonConsumer.put(Constants.CONSUMER_ID, key.getId());
                jsonConsumer.put(Constants.PRICE, value.getPrice());
                jsonConsumer.put(Constants.REMAINED_CONTRACT_MONTHS, value.getMonthsLeft());
                jsonCons.add(jsonConsumer);
            });
            jsonDistributor.put(Constants.CONTRACTS, jsonCons);

            jsonDistributors.add(jsonDistributor);
        });
        obj.put(Constants.DISTRIBUTORS, jsonDistributors);

        try {
            BufferedWriter file = Files.newBufferedWriter(Path.of(outputPath));
            ObjectMapper mapper = new ObjectMapper();
            file.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

