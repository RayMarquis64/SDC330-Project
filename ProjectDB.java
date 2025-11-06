import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * ProjectDB Class
 * Manages database operations for both projects and materials.
 * Provides CRUD operations and file-based persistence.
 */
public class ProjectDB {
    // Attributes
    private Map<String, Project> projects;           // Dictionary of projects (key: project name)
    private Map<String, Material> materials;         // Dictionary of materials (key: material name)
    private String projectDatabaseFile;              // Path to projects database file
    private String materialDatabaseFile;             // Path to materials database file
    
    /**
     * Constructor - Initializes the database with file paths
     * @param projectDatabaseFile Path to projects database file
     * @param materialDatabaseFile Path to materials database file
     */
    public ProjectDB(String projectDatabaseFile, String materialDatabaseFile) {
        this.projects = new HashMap<>();
        this.materials = new HashMap<>();
        this.projectDatabaseFile = projectDatabaseFile;
        this.materialDatabaseFile = materialDatabaseFile;
        
        // Load existing data from files
        loadMaterials();
        loadProjects();
    }
    
    /**
     * Default constructor - uses default file names
     */
    public ProjectDB() {
        this("projects.db", "materials.db");
    }
    
    // ==================== MATERIAL OPERATIONS ====================
    
    /**
     * Adds a new material to the database
     * @param material Material object to add
     * @return true if successful, false if material already exists
     */
    public boolean addMaterial(Material material) {
        if (materials.containsKey(material.getName())) {
            return false; // Material already exists
        }
        materials.put(material.getName(), material);
        saveMaterials();
        return true;
    }
    
    /**
     * Retrieves a material from the database by name
     * @param materialName Name of the material
     * @return Material object or null if not found
     */
    public Material getMaterial(String materialName) {
        return materials.get(materialName);
    }
    
    /**
     * Deletes a material from the database
     * @param materialName Name of material to delete
     * @return true if successful, false if material doesn't exist
     */
    public boolean deleteMaterial(String materialName) {
        if (!materials.containsKey(materialName)) {
            return false;
        }
        materials.remove(materialName);
        saveMaterials();
        return true;
    }
    
    /**
     * Updates an existing material's cost information
     * @param materialName Name of material to update
     * @param newTotalCost New total cost paid
     * @param newTotalVolume New total volume purchased
     * @return true if successful, false if material doesn't exist
     */
    public boolean updateMaterial(String materialName, double newTotalCost, double newTotalVolume) {
        Material material = materials.get(materialName);
        if (material == null) {
            return false;
        }
        material.updateCost(newTotalCost, newTotalVolume);
        saveMaterials();
        return true;
    }
    
    /**
     * Gets all materials in the database
     * @return Map of all materials
     */
    public Map<String, Material> getAllMaterials() {
        return new HashMap<>(materials);
    }
    
    /**
     * Lists all materials with their information
     * @return Formatted string of all materials
     */
    public String listMaterials() {
        if (materials.isEmpty()) {
            return "No materials in database.";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== MATERIALS DATABASE ===\n");
        for (Material material : materials.values()) {
            sb.append(material.toString()).append("\n");
        }
        return sb.toString();
    }
    
    // ==================== PROJECT OPERATIONS ====================
    
    /**
     * Adds a new project to the database
     * @param project Project object to save
     * @return true if successful, false if project already exists
     */
    public boolean addProject(Project project) {
        if (projects.containsKey(project.getProjectName())) {
            return false; // Project already exists
        }
        projects.put(project.getProjectName(), project);
        saveProjects();
        return true;
    }
    
    /**
     * Retrieves a project from the database by name
     * @param projectName Name of the project
     * @return Project object or null if not found
     */
    public Project getProject(String projectName) {
        return projects.get(projectName);
    }
    
    /**
     * Deletes a project from the database
     * @param projectName Name of project to delete
     * @return true if successful, false if project doesn't exist
     */
    public boolean deleteProject(String projectName) {
        if (!projects.containsKey(projectName)) {
            return false;
        }
        projects.remove(projectName);
        saveProjects();
        return true;
    }
    
    /**
     * Updates an existing project with new information
     * @param projectName Name of project to update
     * @param updatedProject Updated Project object
     * @return true if successful, false if project doesn't exist
     */
    public boolean updateProject(String projectName, Project updatedProject) {
        if (!projects.containsKey(projectName)) {
            return false;
        }
        projects.remove(projectName);
        projects.put(updatedProject.getProjectName(), updatedProject);
        saveProjects();
        return true;
    }
    
    /**
     * Gets all projects in the database
     * @return Map of all projects
     */
    public Map<String, Project> getAllProjects() {
        return new HashMap<>(projects);
    }
    
    /**
     * Lists all projects with their basic information
     * @return Formatted string of all projects
     */
    public String listProjects() {
        if (projects.isEmpty()) {
            return "No projects in database.";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== PROJECTS DATABASE ===\n");
        for (Project project : projects.values()) {
            sb.append(project.toSimpleString()).append("\n");
        }
        return sb.toString();
    }
    
    // ==================== FILE PERSISTENCE OPERATIONS ====================
    
    /**
     * Saves all materials to the database file
     */
    private void saveMaterials() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(materialDatabaseFile))) {
            for (Material material : materials.values()) {
                writer.write(material.toDatabaseString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving materials: " + e.getMessage());
        }
    }
    
    /**
     * Loads all materials from the database file
     */
    private void loadMaterials() {
        File file = new File(materialDatabaseFile);
        if (!file.exists()) {
            return; // No file to load from
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(materialDatabaseFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Material material = Material.fromDatabaseString(line);
                    materials.put(material.getName(), material);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading materials: " + e.getMessage());
        }
    }
    
    /**
     * Saves all projects to the database file
     */
    private void saveProjects() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(projectDatabaseFile))) {
            for (Project project : projects.values()) {
                writer.write(project.toDatabaseString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving projects: " + e.getMessage());
        }
    }
    
    /**
     * Loads all projects from the database file
     */
    private void loadProjects() {
        File file = new File(projectDatabaseFile);
        if (!file.exists()) {
            return; // No file to load from
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(projectDatabaseFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    // Parse the database string to get material name
                    String[] parts = line.split("\\|");
                    if (parts.length >= 5) {
                        String materialName = parts[4];
                        Material material = materials.get(materialName);
                        
                        if (material != null) {
                            Project project = Project.fromDatabaseString(line, material);
                            projects.put(project.getProjectName(), project);
                        } else {
                            System.err.println("Warning: Material '" + materialName + 
                                             "' not found for project. Skipping project.");
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading projects: " + e.getMessage());
        }
    }
    
    /**
     * Gets the number of projects in the database
     * @return Number of projects
     */
    public int getProjectCount() {
        return projects.size();
    }
    
    /**
     * Gets the number of materials in the database
     * @return Number of materials
     */
    public int getMaterialCount() {
        return materials.size();
    }
    
    /**
     * Clears all projects from the database (and file)
     */
    public void clearAllProjects() {
        projects.clear();
        saveProjects();
    }
    
    /**
     * Clears all materials from the database (and file)
     */
    public void clearAllMaterials() {
        materials.clear();
        saveMaterials();
    }
}