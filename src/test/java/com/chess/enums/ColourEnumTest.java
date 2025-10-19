package com.chess.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ColourEnumTest {
    
    @Test
    public void testAllColoursExist() {
        // Test that all expected colors are defined
        ColourEnum[] expectedColours = {ColourEnum.WHITE, ColourEnum.BLACK, ColourEnum.BLANK};
        
        for (ColourEnum colour : expectedColours) {
            assertNotNull("Colour " + colour + " should not be null", colour);
        }
    }
    
    @Test
    public void testEnumValues() {
        // Test that enum values() method works correctly
        ColourEnum[] colours = ColourEnum.values();
        assertEquals("Should have 3 colour types", 3, colours.length);
        
        // Test that all expected colours are in the values array
        assertTrue("Should contain WHITE", java.util.Arrays.asList(colours).contains(ColourEnum.WHITE));
        assertTrue("Should contain BLACK", java.util.Arrays.asList(colours).contains(ColourEnum.BLACK));
        assertTrue("Should contain BLANK", java.util.Arrays.asList(colours).contains(ColourEnum.BLANK));
    }
    
    @Test
    public void testColourNames() {
        // Test that colour names are as expected
        assertEquals("WHITE should have name 'WHITE'", "WHITE", ColourEnum.WHITE.name());
        assertEquals("BLACK should have name 'BLACK'", "BLACK", ColourEnum.BLACK.name());
        assertEquals("BLANK should have name 'BLANK'", "BLANK", ColourEnum.BLANK.name());
    }
    
    @Test
    public void testColourOrdinal() {
        // Test that colour ordinals are as expected
        assertEquals("WHITE should have ordinal 0", 0, ColourEnum.WHITE.ordinal());
        assertEquals("BLACK should have ordinal 1", 1, ColourEnum.BLACK.ordinal());
        assertEquals("BLANK should have ordinal 2", 2, ColourEnum.BLANK.ordinal());
    }
    
    @Test
    public void testValueOf() {
        // Test that valueOf method works correctly
        assertEquals("valueOf('WHITE') should return WHITE", ColourEnum.WHITE, ColourEnum.valueOf("WHITE"));
        assertEquals("valueOf('BLACK') should return BLACK", ColourEnum.BLACK, ColourEnum.valueOf("BLACK"));
        assertEquals("valueOf('BLANK') should return BLANK", ColourEnum.BLANK, ColourEnum.valueOf("BLANK"));
    }
    
    @Test
    public void testToString() {
        // Test that toString method works correctly
        assertEquals("WHITE toString should be 'WHITE'", "WHITE", ColourEnum.WHITE.toString());
        assertEquals("BLACK toString should be 'BLACK'", "BLACK", ColourEnum.BLACK.toString());
        assertEquals("BLANK toString should be 'BLANK'", "BLANK", ColourEnum.BLANK.toString());
    }
    
    @Test
    public void testEquality() {
        // Test that colours are equal to themselves
        assertEquals("WHITE should equal WHITE", ColourEnum.WHITE, ColourEnum.WHITE);
        assertEquals("BLACK should equal BLACK", ColourEnum.BLACK, ColourEnum.BLACK);
        assertEquals("BLANK should equal BLANK", ColourEnum.BLANK, ColourEnum.BLANK);
        
        // Test that different colours are not equal
        assertNotEquals("WHITE should not equal BLACK", ColourEnum.WHITE, ColourEnum.BLACK);
        assertNotEquals("WHITE should not equal BLANK", ColourEnum.WHITE, ColourEnum.BLANK);
        assertNotEquals("BLACK should not equal BLANK", ColourEnum.BLACK, ColourEnum.BLANK);
    }
    
    @Test
    public void testHashCode() {
        // Test that hashCode is consistent
        assertEquals("WHITE hashCode should be consistent", 
                    ColourEnum.WHITE.hashCode(), ColourEnum.WHITE.hashCode());
        assertEquals("BLACK hashCode should be consistent", 
                    ColourEnum.BLACK.hashCode(), ColourEnum.BLACK.hashCode());
        assertEquals("BLANK hashCode should be consistent", 
                    ColourEnum.BLANK.hashCode(), ColourEnum.BLANK.hashCode());
    }
    
    @Test
    public void testCompareTo() {
        // Test that compareTo works correctly based on ordinal
        assertTrue("WHITE should be less than BLACK", ColourEnum.WHITE.compareTo(ColourEnum.BLACK) < 0);
        assertTrue("BLACK should be less than BLANK", ColourEnum.BLACK.compareTo(ColourEnum.BLANK) < 0);
        assertTrue("WHITE should be less than BLANK", ColourEnum.WHITE.compareTo(ColourEnum.BLANK) < 0);
        
        assertTrue("BLACK should be greater than WHITE", ColourEnum.BLACK.compareTo(ColourEnum.WHITE) > 0);
        assertTrue("BLANK should be greater than BLACK", ColourEnum.BLANK.compareTo(ColourEnum.BLACK) > 0);
        assertTrue("BLANK should be greater than WHITE", ColourEnum.BLANK.compareTo(ColourEnum.WHITE) > 0);
        
        assertEquals("WHITE should equal WHITE in compareTo", 0, ColourEnum.WHITE.compareTo(ColourEnum.WHITE));
        assertEquals("BLACK should equal BLACK in compareTo", 0, ColourEnum.BLACK.compareTo(ColourEnum.BLACK));
        assertEquals("BLANK should equal BLANK in compareTo", 0, ColourEnum.BLANK.compareTo(ColourEnum.BLANK));
    }
}
