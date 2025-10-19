package com.chess;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import com.chess.dto.BlockTest;
import com.chess.dto.ChessBoardTest;
import com.chess.dto.GameTest;
import com.chess.dto.MoveTest;
import com.chess.dto.PlayerTest;
import com.chess.enums.ColourEnumTest;
import com.chess.enums.PiecesEnumTest;

/**
 * Test runner class to execute all chess game tests
 */
public class TestRunner {
    
    public static void main(String[] args) {
        System.out.println("Running Chess Game Test Suite...\n");
        
        // Run all test classes
        Class<?>[] testClasses = {
            PlayerTest.class,
            MoveTest.class,
            BlockTest.class,
            GameTest.class,
            ChessBoardTest.class,
            PiecesEnumTest.class,
            ColourEnumTest.class
        };
        
        Result result = JUnitCore.runClasses(testClasses);
        
        // Print results
        System.out.println("Test Results:");
        System.out.println("=============");
        System.out.println("Tests run: " + result.getRunCount());
        System.out.println("Failures: " + result.getFailureCount());
        System.out.println("Ignored: " + result.getIgnoreCount());
        System.out.println("Time: " + result.getRunTime() + "ms");
        
        if (result.wasSuccessful()) {
            System.out.println("\n✅ All tests passed successfully!");
        } else {
            System.out.println("\n❌ Some tests failed:");
            for (Failure failure : result.getFailures()) {
                System.out.println("  - " + failure.getTestHeader() + ": " + failure.getMessage());
            }
        }
        
        System.exit(result.wasSuccessful() ? 0 : 1);
    }
}
