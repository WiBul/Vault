package com.example.thevault.support;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

//import static org.junit.jupiter.api.Assertions.*;

class BSNvalidatorTest {
    long bsn;
    long bsnFalse;

    @BeforeEach
    void setUp() {
        bsn = 999998031; //is een geldige BSN (zie lijst in resources)
        bsnFalse = 123456789; //is een ongeldig BSN
    }

    @Test
    void bsnValideren() {
        boolean validatie = BSNvalidator.bsnValideren(bsn);
        assertThat(validatie).isTrue();
    }

    @Test
    void bsnValiderenFalse() {
        boolean validatie = BSNvalidator.bsnValideren(bsnFalse);
        assertThat(validatie).isFalse();
    }
}