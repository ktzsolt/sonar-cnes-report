package fr.cnes.sonar.report.exporters.data;

import fr.cnes.sonar.report.CommonTest;
import fr.cnes.sonar.report.model.Report;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class PlaceHoldersDebuggingTest extends CommonTest {

    @Test
    public void debugPlaceHoldersMapping() {
        // Use the existing CommonTest setup method
        Report report = getReport();
        Map<String, Double> metricStats = new HashMap<>();
        
        // Set up the metric stats with the values that should appear in placeholders
        metricStats.put("maxcomplexity", 32.0);  // Should appear in XX-MAXCOMPLEXITY-XX
        metricStats.put("maxcognitive_complexity", 45.0);  // Should appear in XX-MAXCOGNITIVECOMPLEXITY-XX
        metricStats.put("maxncloc", 200.0);  // Should appear in XX-MAXNCLOC-XX
        metricStats.put("maxcomment_lines_density", 30.0);  // Should appear in XX-MAXCOMMENTDENSITY-XX
        metricStats.put("maxcoverage", 92.0);  // Should appear in XX-MAXCOVERAGE-XX
        metricStats.put("maxduplicated_lines_density", 8.0);  // Should appear in XX-MAXDUPLICATION-XX
        
        // Add the min values too for completeness
        metricStats.put("mincomplexity", 8.0);
        metricStats.put("mincognitive_complexity", 12.0);
        metricStats.put("minncloc", 50.0);
        metricStats.put("mincomment_lines_density", 15.2);
        metricStats.put("mincoverage", 78.0);
        metricStats.put("minduplicated_lines_density", 2.0);
        
        report.setMetricsStats(metricStats);
        
        try {
            // Test the placeholders map
            Map<String, String> placeholders = PlaceHolders.loadPlaceholdersMap(report);
            
            System.out.println("=== DEBUGGING PLACEHOLDERS ===");
            System.out.println("XX-MAXCOMPLEXITY-XX: " + placeholders.get("XX-MAXCOMPLEXITY-XX"));
            System.out.println("XX-MAXCOGNITIVECOMPLEXITY-XX: " + placeholders.get("XX-MAXCOGNITIVECOMPLEXITY-XX"));
            System.out.println("XX-MAXNCLOC-XX: " + placeholders.get("XX-MAXNCLOC-XX"));
            System.out.println("XX-MAXCOMMENTDENSITY-XX: " + placeholders.get("XX-MAXCOMMENTDENSITY-XX"));
            System.out.println("XX-MAXCOVERAGE-XX: " + placeholders.get("XX-MAXCOVERAGE-XX"));
            System.out.println("XX-MAXDUPLICATION-XX: " + placeholders.get("XX-MAXDUPLICATION-XX"));
            
            // These should show the MAX values, not SUM
            assertEquals("32.0", placeholders.get("XX-MAXCOMPLEXITY-XX"));
            assertEquals("45.0", placeholders.get("XX-MAXCOGNITIVECOMPLEXITY-XX"));
            assertEquals("200.0", placeholders.get("XX-MAXNCLOC-XX"));
            assertEquals("30.0", placeholders.get("XX-MAXCOMMENTDENSITY-XX"));
            assertEquals("92.0", placeholders.get("XX-MAXCOVERAGE-XX"));
            assertEquals("8.0", placeholders.get("XX-MAXDUPLICATION-XX"));
            
        } catch (Exception e) {
            System.out.println("Error in placeholders: " + e.getMessage());
            e.printStackTrace();
            
            // If we got a NullPointerException, it means some keys are missing from metricStats
            // This would indicate a mismatch in the STATKEY constants
            if (e instanceof NullPointerException) {
                System.out.println("Missing keys in metricStats map - checking what's actually in the map:");
                for (Map.Entry<String, Double> entry : metricStats.entrySet()) {
                    System.out.println("  " + entry.getKey() + " = " + entry.getValue());
                }
            }
        }
    }
}