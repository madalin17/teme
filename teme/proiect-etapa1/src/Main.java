import fileio.Input;
import fileio.InputLoader;

public final class Main {

    private Main() { }

    /**
     * Main method
     * @param args not used
     * @throws Exception
     */
    public static void main(final String[] args) throws Exception {
        InputLoader inputLoader = new InputLoader("/home/madalin/II-Sem1/POO/Etapa1/teme/teme"
                + "/proiect-etapa1/checker/resources/in/basic_1.json");
        Input input = inputLoader.readData();

        System.out.println(input.getNumberOfTurns());
        System.out.println(input.getConsumers());
        System.out.println(input.getDistributors());
        System.out.println(input.getMonthlyUpdates());
    }
}
