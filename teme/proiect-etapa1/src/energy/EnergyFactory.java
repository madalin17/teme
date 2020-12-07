package energy;

import constants.Constants;
import org.json.simple.JSONObject;

public final class EnergyFactory {

    private static EnergyFactory factory;

    static {
        factory = null;
    }

    private EnergyFactory() { }

    /**
     * @return singleton instance of class Factory
     */
    public static EnergyFactory getFactory() {
        if (factory == null) {
            factory = new EnergyFactory();
        }
        return factory;
    }

    /**
     * Function creates a new consumer/distributor
     * @param json JSONObject to be parsed
     * @param instance string that tells Factory if it should create consumer or distributor
     * @return allocated EnergyInstance
     */
    public EnergyInstance createEnergyInstance(final Object json, final String instance) {
        return switch (instance) {
            case Constants.CONSUMERS -> new Consumer(
                    ((Long) ((JSONObject) json).get(Constants.ID)).intValue(),
                    ((Long) ((JSONObject) json).get(Constants.INITIAL_BUDGET)).intValue(),
                    ((Long) ((JSONObject) json).get(Constants.MONTHLY_INCOME)).intValue());
            case Constants.DISTRIBUTORS -> new Distributor(
                    ((Long) ((JSONObject) json).get(Constants.ID)).intValue(),
                    ((Long) ((JSONObject) json).get(Constants.CONTRACT_LENGTH)).intValue(),
                    ((Long) ((JSONObject) json).get(Constants.INITIAL_BUDGET)).intValue(),
                    ((Long) ((JSONObject) json)
                            .get(Constants.INITIAL_INFRASTRUCTURE_COST)).intValue(),
                    ((Long) ((JSONObject) json)
                            .get(Constants.INITIAL_PRODUCTION_COST)).intValue());
            default -> null;
        };
    }
}
