import java.util.Scanner;

/**
 * Main Application Class for 3D Printing Cost Calculator
 * Provides a menu-driven interface for managing projects and materials
 */
public class App {
    private static ProjectDB database;
    private static Scanner scanner;
    
    public static void main(String[] args) {
        database = new ProjectDB();
        scanner = new Scanner(System.in);
        
        System.out.println("======================================");
        System.out.println("3D PRINTING COST CALCULATOR");
        System.out.println("======================================");
        
        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    addNewMaterial();
                    break;
                case 2:
                    viewAllMaterials();
                    break;
                case 3:
                    updateMaterialCost();
                    break;
                case 4:
                    addNewProject();
                    break;
                case 5:
                    viewProject();
                    break;
                case 6:
                    viewAllProjects();
                    break;
                case 7:
                    updateProject();
                    break;
                case 8:
                    deleteProject();
                    break;
                case 9:
                    System.out.println("\nThank you for using 3D Printing Cost Calculator!");
                    running = false;
                    break;
                default:
                    System.out.println("\nInvalid choice. Please try again.");
            }
        }
        
        scanner.close();
    }
    
    /**
     * Displays the main menu
     */
    private static void displayMainMenu() {
        System.out.println("\n======================================");
        System.out.println("MAIN MENU");
        System.out.println("======================================");
        System.out.println("Materials Management:");
        System.out.println("  1. Add New Material");
        System.out.println("  2. View All Materials");
        System.out.println("  3. Update Material Cost");
        System.out.println("\nProject Management:");
        System.out.println("  4. Add New Project");
        System.out.println("  5. View Project Details");
        System.out.println("  6. View All Projects");
        System.out.println("  7. Update Project");
        System.out.println("  8. Delete Project");
        System.out.println("\n  9. Exit");
        System.out.println("======================================");
    }
    
    /**
     * Adds a new material to the database
     */
    private static void addNewMaterial() {
        System.out.println("\n--- ADD NEW MATERIAL ---");
        
        String name = getStringInput("Material name (e.g., PLA, ABS, PETG): ");
        double totalCost = getDoubleInput("Total cost paid for material: $");
        double totalVolume = getDoubleInput("Total weight/volume in grams: ");
        
        Material material = new Material(name, totalCost, totalVolume);
        
        if (database.addMaterial(material)) {
            System.out.println("\n✓ Material added successfully!");
            System.out.println(material);
        } else {
            System.out.println("\n✗ Material with this name already exists.");
        }
    }
    
    /**
     * Views all materials in the database
     */
    private static void viewAllMaterials() {
        System.out.println("\n" + database.listMaterials());
    }
    
    /**
     * Updates the cost of an existing material
     */
    private static void updateMaterialCost() {
        System.out.println("\n--- UPDATE MATERIAL COST ---");
        
        viewAllMaterials();
        
        String name = getStringInput("\nMaterial name to update: ");
        Material material = database.getMaterial(name);
        
        if (material == null) {
            System.out.println("✗ Material not found.");
            return;
        }
        
        System.out.println("\nCurrent information:");
        System.out.println(material);
        
        double newTotalCost = getDoubleInput("\nNew total cost paid: $");
        double newTotalVolume = getDoubleInput("New total weight/volume in grams: ");
        
        if (database.updateMaterial(name, newTotalCost, newTotalVolume)) {
            System.out.println("\n✓ Material updated successfully!");
            System.out.println(database.getMaterial(name));
        } else {
            System.out.println("\n✗ Error updating material.");
        }
    }
    
    /**
     * Adds a new project to the database
     */
    private static void addNewProject() {
        System.out.println("\n--- ADD NEW PROJECT ---");
        
        // Check if materials exist
        if (database.getMaterialCount() == 0) {
            System.out.println("✗ No materials in database. Please add a material first.");
            return;
        }
        
        viewAllMaterials();
        
        String projectName = getStringInput("\nProject name: ");
        double designTime = getDoubleInput("Design time (hours): ");
        double printTime = getDoubleInput("Print time (hours): ");
        double materialUsed = getDoubleInput("Material used (grams): ");
        
        String materialName = getStringInput("Material type: ");
        Material material = database.getMaterial(materialName);
        
        if (material == null) {
            System.out.println("✗ Material not found. Please select a valid material.");
            return;
        }
        
        double hourlyRate = getDoubleInput("Your hourly design rate: $");
        double printRate = getDoubleInput("Printer operation cost per hour: $");
        
        Project project = new Project(projectName, designTime, printTime, 
                                     materialUsed, material, hourlyRate, printRate);
        
        if (database.addProject(project)) {
            System.out.println("\n✓ Project added successfully!");
            System.out.println("\n" + project);
        } else {
            System.out.println("\n✗ Project with this name already exists.");
        }
    }
    
    /**
     * Views details of a specific project
     */
    private static void viewProject() {
        System.out.println("\n--- VIEW PROJECT ---");
        
        String projectName = getStringInput("Project name: ");
        Project project = database.getProject(projectName);
        
        if (project != null) {
            System.out.println("\n" + project);
        } else {
            System.out.println("✗ Project not found.");
        }
    }
    
    /**
     * Views all projects in the database
     */
    private static void viewAllProjects() {
        System.out.println("\n" + database.listProjects());
    }
    
    /**
     * Updates an existing project
     */
    private static void updateProject() {
        System.out.println("\n--- UPDATE PROJECT ---");
        
        viewAllProjects();
        
        String projectName = getStringInput("\nProject name to update: ");
        Project oldProject = database.getProject(projectName);
        
        if (oldProject == null) {
            System.out.println("✗ Project not found.");
            return;
        }
        
        System.out.println("\nCurrent project details:");
        System.out.println(oldProject);
        
        System.out.println("\nEnter new values (or press Enter to keep current):");
        
        String newName = getStringInputOptional("New project name [" + oldProject.getProjectName() + "]: ", 
                                               oldProject.getProjectName());
        double designTime = getDoubleInputOptional("Design time [" + oldProject.getDesignTime() + "]: ", 
                                                   oldProject.getDesignTime());
        double printTime = getDoubleInputOptional("Print time [" + oldProject.getPrintTime() + "]: ", 
                                                  oldProject.getPrintTime());
        double materialUsed = getDoubleInputOptional("Material used [" + oldProject.getMaterialUsed() + "]: ", 
                                                     oldProject.getMaterialUsed());
        
        Project updatedProject = new Project(newName, designTime, printTime, materialUsed,
                                           oldProject.getMaterialType(), 
                                           oldProject.getHourlyRate(), 
                                           oldProject.getPrintRate());
        
        if (database.updateProject(projectName, updatedProject)) {
            System.out.println("\n✓ Project updated successfully!");
            System.out.println("\n" + updatedProject);
        } else {
            System.out.println("\n✗ Error updating project.");
        }
    }
    
    /**
     * Deletes a project from the database
     */
    private static void deleteProject() {
        System.out.println("\n--- DELETE PROJECT ---");
        
        viewAllProjects();
        
        String projectName = getStringInput("\nProject name to delete: ");
        String confirm = getStringInput("Are you sure you want to delete '" + projectName + "'? (yes/no): ");
        
        if (confirm.equalsIgnoreCase("yes")) {
            if (database.deleteProject(projectName)) {
                System.out.println("\n✓ Project deleted successfully.");
            } else {
                System.out.println("\n✗ Project not found.");
            }
        } else {
            System.out.println("\nDeletion cancelled.");
        }
    }
    
    // ==================== INPUT HELPER METHODS ====================
    
    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    private static String getStringInputOptional(String prompt, String defaultValue) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? defaultValue : input;
    }
    
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
    
    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = Double.parseDouble(scanner.nextLine().trim());
                if (value < 0) {
                    System.out.println("Please enter a positive number.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
    
    private static double getDoubleInputOptional(String prompt, double defaultValue) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Using default value.");
            return defaultValue;
        }
    }
}