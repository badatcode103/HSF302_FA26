package org.fa26.de190686;

import org.fa26.de190686.pojo.Employee;
import org.fa26.de190686.pojo.Project;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class Hsf302Slot4ApplicationTests {

    @Test
    void entitiesCanBeCreatedWithoutSpringContext() {
        assertNotNull(new Employee());
        assertNotNull(new Project());
    }
}
