/**
 * Project Class
 * Represents a 3D printing project with complete cost calculation.
 * Tracks design time, print time, material usage, and calculates total cost.
 */
public class Project {
    // Attributes
    private String projectName;
    private double designTime;      // Hours spent on design
    private double printTime;       // Hours spent printing
    private double materialUsed;    // Grams of material used
    private Material materialType;  // Type of material used
    private double hourlyRate;      // Cost per hour of design work
    private double printRate;       // Cost per hour of printer operation
    private double totalCost;       // Calculated total cost
    
    /**
     * Constructor - Creates a new project with all necessary parameters
     * @param projectName Name of the project
     * @param designTime Hours spent on design
     * @param printTime Hours spent printing
     * @param materialUsed Grams of material used
     * @param materialType Material object representing the filament used
     * @param hourlyRate Designer's hourly rate
     * @param printRate Printer operation cost per hour
     */
    public Project(String projectName, double designTime, double printTime, 
                   double materialUsed, Material materialType, 
                   double hourlyRate, double printRate) {
        this.projectName = projectName;
        this.designTime = designTime;
        this.printTime = printTime;
        this.materialUsed = materialUsed;
        this.materialType = materialType;
        this.hourlyRate = hourlyRate;
        this.printRate = printRate;
        this.totalCost = calculateCost();
    }
    
    /**
     * Calculates the total cost of the project
     * Combines design cost, print cost, and material cost
     * @return Total project cost
     */
    public double calculateCost() {
        double designCost = calculateDesignCost();
        double printCost = calculatePrintCost();
        double materialCost = calculateMaterialCost();
        totalCost = designCost + printCost + materialCost;
        return totalCost;
    }
    
    /**
     * Calculates the cost of design work
     * @return Design cost (design time * hourly rate)
     */
    public double calculateDesignCost() {
        return designTime * hourlyRate;
    }
    
    /**
     * Calculates the cost of printer operation
     * @return Print cost (print time * print rate)
     */
    public double calculatePrintCost() {
        return printTime * printRate;
    }
    
    /**
     * Calculates the cost of material used
     * @return Material cost (material used * cost per gram)
     */
    public double calculateMaterialCost() {
        return materialUsed * materialType.getCostPerGram();
    }
    
    /**
     * Gets the project name
     * @return Project name
     */
    public String getProjectName() {
        return projectName;
    }
    
    /**
     * Gets the total cost
     * @return Total project cost
     */
    public double getTotalCost() {
        return totalCost;
    }
    
    /**
     * Gets design time
     * @return Hours spent on design
     */
    public double getDesignTime() {
        return designTime;
    }
    
    /**
     * Gets print time
     * @return Hours spent printing
     */
    public double getPrintTime() {
        return printTime;
    }
    
    /**
     * Gets material used
     * @return Grams of material used
     */
    public double getMaterialUsed() {
        return materialUsed;
    }
    
    /**
     * Gets the material type
     * @return Material object
     */
    public Material getMaterialType() {
        return materialType;
    }
    
    /**
     * Gets hourly rate
     * @return Designer's hourly rate
     */
    public double getHourlyRate() {
        return hourlyRate;
    }
    
    /**
     * Gets print rate
     * @return Printer operation cost per hour
     */
    public double getPrintRate() {
        return printRate;
    }
    
    /**
     * Sets the project name
     * @param projectName New project name
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    
    /**
     * Sets design time and recalculates cost
     * @param designTime New design time in hours
     */
    public void setDesignTime(double designTime) {
        this.designTime = designTime;
        calculateCost();
    }
    
    /**
     * Sets print time and recalculates cost
     * @param printTime New print time in hours
     */
    public void setPrintTime(double printTime) {
        this.printTime = printTime;
        calculateCost();
    }
    
    /**
     * Sets material used and recalculates cost
     * @param materialUsed New material usage in grams
     */
    public void setMaterialUsed(double materialUsed) {
        this.materialUsed = materialUsed;
        calculateCost();
    }
    
    /**
     * Converts project to database string format
     * @return String representation for database storage
     */
    public String toDatabaseString() {
        return String.format("%s|%.2f|%.2f|%.2f|%s|%.2f|%.2f|%.2f",
                           projectName, designTime, printTime, materialUsed,
                           materialType.getName(), hourlyRate, printRate, totalCost);
    }
    
    /**
     * Creates a Project object from a database string
     * Requires a Material object to be passed in (looked up by name)
     * @param dbString Database string
     * @param material Material object for this project
     * @return Project object
     */
    public static Project fromDatabaseString(String dbString, Material material) {
        String[] parts = dbString.split("\\|");
        if (parts.length != 8) {
            throw new IllegalArgumentException("Invalid database string format");
        }
        
        String projectName = parts[0];
        double designTime = Double.parseDouble(parts[1]);
        double printTime = Double.parseDouble(parts[2]);
        double materialUsed = Double.parseDouble(parts[3]);
        // parts[4] is material name - already have the Material object
        double hourlyRate = Double.parseDouble(parts[5]);
        double printRate = Double.parseDouble(parts[6]);
        
        return new Project(projectName, designTime, printTime, materialUsed,
                         material, hourlyRate, printRate);
    }
    
    /**
     * Returns a formatted string with project details and cost breakdown
     * @return Complete project information as formatted string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=====================================\n");
        sb.append("PROJECT: ").append(projectName).append("\n");
        sb.append("=====================================\n");
        sb.append(String.format("Design Time: %.2f hours @ $%.2f/hr = $%.2f\n", 
                  designTime, hourlyRate, calculateDesignCost()));
        sb.append(String.format("Print Time: %.2f hours @ $%.2f/hr = $%.2f\n", 
                  printTime, printRate, calculatePrintCost()));
        sb.append(String.format("Material: %.2fg of %s @ $%.4f/g = $%.2f\n", 
                  materialUsed, materialType.getName(), 
                  materialType.getCostPerGram(), calculateMaterialCost()));
        sb.append("-------------------------------------\n");
        sb.append(String.format("TOTAL COST: $%.2f\n", totalCost));
        sb.append("=====================================");
        return sb.toString();
    }
    
    /**
     * Returns a simple summary of the project (project name and cost)
     * @return Simple project summary
     */
    public String toSimpleString() {
        return String.format("%s: $%.2f", projectName, totalCost);
    }
}