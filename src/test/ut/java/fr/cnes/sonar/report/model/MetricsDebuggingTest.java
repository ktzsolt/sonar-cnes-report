package fr.cnes.sonar.report.model;

import fr.cnes.sonar.report.CommonTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MetricsDebuggingTest extends CommonTest {

    @Test
    public void debugMetricsIssue() {
        // Create test components data similar to real SonarQube data
        ArrayList<Map<String,String>> componentsTest = new ArrayList<>();
        ComponentsWrapper components = new ComponentsWrapper();

        // Component 1 - complexity 10, cognitive 15, ncloc 100
        Map<String,String> component1 = new HashMap<>();
        component1.put("complexity", "10");
        component1.put("cognitive_complexity", "15");
        component1.put("ncloc", "100");
        component1.put("comment_lines_density", "20.5");
        component1.put("coverage", "85.0");
        component1.put("duplicated_lines_density", "5.0");
        
        // Component 2 - complexity 32 (should be max), cognitive 45, ncloc 200
        Map<String,String> component2 = new HashMap<>();
        component2.put("complexity", "32");
        component2.put("cognitive_complexity", "45");
        component2.put("ncloc", "200");
        component2.put("comment_lines_density", "15.2");
        component2.put("coverage", "92.0");
        component2.put("duplicated_lines_density", "2.0");
        
        // Component 3 - complexity 8, cognitive 12, ncloc 50
        Map<String,String> component3 = new HashMap<>();
        component3.put("complexity", "8");
        component3.put("cognitive_complexity", "12");
        component3.put("ncloc", "50");
        component3.put("comment_lines_density", "30.0");
        component3.put("coverage", "78.0");
        component3.put("duplicated_lines_density", "8.0");
        
        componentsTest.add(component1);
        componentsTest.add(component2);
        componentsTest.add(component3);
        components.setComponentsList(componentsTest);

        // Get the metric stats
        Map<String, Double> metricStats = components.getMetricStats();
        
        // Print out what we actually get vs what we expect
        System.out.println("=== DEBUGGING METRICS ===");
        System.out.println("Expected maxcomplexity: 32.0, Actual: " + metricStats.get("maxcomplexity"));
        System.out.println("Expected maxcognitive_complexity: 45.0, Actual: " + metricStats.get("maxcognitive_complexity"));
        System.out.println("Expected maxncloc: 200.0, Actual: " + metricStats.get("maxncloc"));
        System.out.println("Expected maxcomment_lines_density: 30.0, Actual: " + metricStats.get("maxcomment_lines_density"));
        System.out.println("Expected maxcoverage: 92.0, Actual: " + metricStats.get("maxcoverage"));
        System.out.println("Expected maxduplicated_lines_density: 8.0, Actual: " + metricStats.get("maxduplicated_lines_density"));
        
        // Test the broken ones - these should return MAX but if bug exists, might return SUM
        double sumComplexity = 10 + 32 + 8; // = 50
        double sumCognitive = 15 + 45 + 12; // = 72
        double sumNcloc = 100 + 200 + 50; // = 350
        
        System.out.println("Sum complexity would be: " + sumComplexity);
        System.out.println("Sum cognitive would be: " + sumCognitive); 
        System.out.println("Sum ncloc would be: " + sumNcloc);
        
        // Assert what we expect (max values)
        assertEquals(32.0, metricStats.get("maxcomplexity"));
        assertEquals(45.0, metricStats.get("maxcognitive_complexity"));
        assertEquals(200.0, metricStats.get("maxncloc"));
        assertEquals(30.0, metricStats.get("maxcomment_lines_density"));
        assertEquals(92.0, metricStats.get("maxcoverage"));
        assertEquals(8.0, metricStats.get("maxduplicated_lines_density"));
    }

    /**
     * Wrapper on Components for testing purposes
     */
    private class ComponentsWrapper extends Components {
        public double getMaxMetricPublic(String metric) {
            return super.getMaxMetric(metric);
        }
        public double getMinMetricPublic(String metric) {
            return super.getMinMetric(metric);
        }
        public double getMedianMetricPublic(String metric) {
            return super.getMedianMetric(metric);
        }
    }
}