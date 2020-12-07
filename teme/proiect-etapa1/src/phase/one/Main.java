package phase.one;

import energy.Consumer;
import energy.Distributor;
import fileio.Input;
import fileio.InputLoader;
import fileio.MonthlyUpdate;
import fileio.OutputLoader;
import java.util.List;

public final class Main {

    private Main() { }

    /**
     * @param args contains path to input file and path to output file
     */
    public static void main(final String[] args) {
        String inputPath = args[0];
        String outputPath = args[1];

        InputLoader inputLoader = new InputLoader(inputPath);
        Input input = inputLoader.readData();

        List<Consumer> consumers = input.getConsumers();
        List<Distributor> distributors = input.getDistributors();
        List<MonthlyUpdate> monthlyUpdates = input.getMonthlyUpdates();

        MonthUpdate.initialUpdate(consumers, distributors);
        for (int i = 0; i < input.getNumberOfTurns(); i++) {
            List<Integer> sizeContractedConsumers = MonthUpdate.removeConsumer(distributors);
            for (int j = 0; j < sizeContractedConsumers.size(); j++) {
                distributors.get(j).setSize(sizeContractedConsumers.get(j));
            }
            MonthUpdate.update(consumers, distributors, monthlyUpdates.get(i));
            MonthUpdate.payments(distributors);
            MonthUpdate.removeBankruptConsumer(consumers, distributors);
            MonthUpdate.bankrupt(distributors);
        }

        OutputLoader outputLoader = new OutputLoader(outputPath, consumers, distributors);
        outputLoader.writeFile();
    }
}
