/**
 * Material Class
 * Represents a 3D printing material with cost tracking capabilities.
 * Stores material name, cost per gram, and total volume purchased.
 */
public class Material {
    // Attributes
    private String name;
    private double costPerGram;
    private double totalVolume;
    
    /**
     * Constructor - calculates cost per gram based on total cost and volume
     * @param name Name of the material (e.g., "PLA", "ABS", "PETG")
     * @param totalCost Total amount paid for the material
     * @param totalVolume Total weight of purchased material in grams
     */
    public Material(String name, double totalCost, double totalVolume) {
        this.name = name;
        this.totalVolume = totalVolume;
        this.costPerGram = calculateCostPerGram(totalCost, totalVolume);
    }
    
    /**
     * Alternative constructor for creating material with known cost per gram
     * @param name Name of the material
     * @param costPerGram Cost per gram of material
     * @param totalVolume Total volume/weight in grams
     */
    public Material(String name, double costPerGram, double totalVolume, boolean isPerGram) {
        this.name = name;
        this.costPerGram = costPerGram;
        this.totalVolume = totalVolume;
    }
    
    /**
     * Calculates cost per gram from total cost and volume
     * @param totalCost Total amount paid
     * @param totalVolume Total weight in grams
     * @return Cost per gram
     */
    private double calculateCostPerGram(double totalCost, double totalVolume) {
        if (totalVolume <= 0) {
            throw new IllegalArgumentException("Total volume must be greater than 0");
        }
        return totalCost / totalVolume;
    }
    
    /**
     * Gets the cost per gram of the material
     * @return Cost per gram
     */
    public double getCostPerGram() {
        return costPerGram;
    }
    
    /**
     * Updates the cost of the material based on new purchase
     * @param newTotalCost New total cost paid
     * @param newTotalVolume New total volume purchased
     */
    public void updateCost(double newTotalCost, double newTotalVolume) {
        this.totalVolume = newTotalVolume;
        this.costPerGram = calculateCostPerGram(newTotalCost, newTotalVolume);
    }
    
    /**
     * Gets the name of the material
     * @return Material name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the total volume
     * @return Total volume in grams
     */
    public double getTotalVolume() {
        return totalVolume;
    }
    
    /**
     * Converts material object to a formatted string representation
     * Used for database storage and display
     * @return String representation in format: name|costPerGram|totalVolume
     */
    public String toDatabaseString() {
        return String.format("%s|%.4f|%.2f", name, costPerGram, totalVolume);
    }
    
    /**
     * Creates a Material object from a database string
     * @param dbString Database string in format: name|costPerGram|totalVolume
     * @return Material object
     */
    public static Material fromDatabaseString(String dbString) {
        String[] parts = dbString.split("\\|");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid database string format");
        }
        String name = parts[0];
        double costPerGram = Double.parseDouble(parts[1]);
        double totalVolume = Double.parseDouble(parts[2]);
        return new Material(name, costPerGram, totalVolume, true);
    }
    
    /**
     * String representation for display purposes
     * @return Formatted string with material information
     */
    @Override
    public String toString() {
        return String.format("Material: %s | Cost per gram: $%.4f | Total volume: %.2fg", 
                           name, costPerGram, totalVolume);
    }
}