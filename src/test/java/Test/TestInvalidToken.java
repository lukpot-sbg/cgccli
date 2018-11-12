/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import com.test.cgccli.*;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Luka Potkonjak
 */
public class TestInvalidToken {
    private CgccliAPICaller cgccliAPICaller;
    private final String invalidToken = "00000000000000000000000000000000";
    
    @Before
    public void setUp() {
        cgccliAPICaller = new CgccliAPICaller(invalidToken);
    }
    
    @Test
    public void issueInvalidTokenRequestTest() {
        List<Project> projectsList = cgccliAPICaller.getProjects();
        // returns null in case of an error, otherwise it would be an empty list
        Assert.assertEquals(null, projectsList);
    }
}
