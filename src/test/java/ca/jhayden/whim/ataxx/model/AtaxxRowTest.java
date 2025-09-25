package ca.jhayden.whim.ataxx.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class AtaxxRowTest {
    @Test
    public void of_validRow() {
        AtaxxRow row = AtaxxRow.of("1.#.2");
        assertEquals("1.#.2", row.toString());
    }

    @Test
    public void of_emptyRow() {
        AtaxxRow row = AtaxxRow.of("");
        assertEquals("", row.toString());
    }

    @Test
    public void of_nullRow() {
        assertThrows(NullPointerException.class, () -> AtaxxRow.of(null));
    }

    @Test
    public void of_trimmedRow() {
        AtaxxRow row = AtaxxRow.of("  1.2  ");
        assertEquals("1.2", row.toString());
    }
}