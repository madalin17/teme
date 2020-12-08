package phase.one;

import fileio.Input;
import fileio.InputLoader;
import fileio.OutputLoader;

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

        Simulation.simulation(input.getConsumers(), input.getDistributors(),
                input.getMonthlyUpdates(), input.getNumberOfTurns());

        OutputLoader outputLoader = new OutputLoader(outputPath,
                input.getConsumers(), input.getDistributors());
        outputLoader.writeFile();
    }
}
