/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import com.test.cgccli.Cgccli;
import java.security.InvalidParameterException;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Luka Potkonjak
 */
public class TestInvalidInputParameters {
    private MockCgccliAPICaller mockCgccliAPICaller;
    
    @Before
    public void setUp() {
        mockCgccliAPICaller = new MockCgccliAPICaller();
    }
    
    @Test(expected = InvalidParameterException.class)
    public void getProjectsInvalidParameterTest1() {
        String[] args = {"--token", "token", "projects", "list", "additionalparameter"};
        Cgccli.parseArgumentsAndCallAPI(args, mockCgccliAPICaller);
    }
    
    @Test(expected = InvalidParameterException.class)
    public void getProjectsInvalidParameterTest2() {
        String[] args = {"token", "projects", "list"};
        Cgccli.parseArgumentsAndCallAPI(args, mockCgccliAPICaller);
    }
    
    @Test(expected = InvalidParameterException.class)
    public void getFilesInvalidParameterTest1() {
        String[] args = {"--token", "token", "files", "list", "--project"};
        Cgccli.parseArgumentsAndCallAPI(args, mockCgccliAPICaller);
    }
    
    @Test(expected = InvalidParameterException.class)
    public void getFilesInvalidParameterTest2() {
        String[] args = {"--token", "token", "files", "list", "project", "user/projectid"};
        Cgccli.parseArgumentsAndCallAPI(args, mockCgccliAPICaller);
    }
    
    @Test(expected = InvalidParameterException.class)
    public void updateFileInvalidParameterTest1() {
        String[] args = {"--token", "token", "files", "update", "--file", "fileid",
        "name=file1", "tags=tag1,tag2", "disalowedproperty=value"};
        Cgccli.parseArgumentsAndCallAPI(args, mockCgccliAPICaller);
    }
    
    @Test(expected = InvalidParameterException.class)
    public void updateFileInvalidParameterTest2() {
        String[] args = {"--token", "token", "files", "update", "--file", "fileid",
        "tags", "metadata.key1=value1"};
        Cgccli.parseArgumentsAndCallAPI(args, mockCgccliAPICaller);
    }
    
    @Test(expected = InvalidParameterException.class)
    public void updateFileInvalidParameterTest3() {
        String[] args = {"--token", "token", "files", "update", "--file", "fileid",
        "something", "metadata.key1=value1"};
        Cgccli.parseArgumentsAndCallAPI(args, mockCgccliAPICaller);
    }
    
    @Test(expected = InvalidParameterException.class)
    public void updateFileInvalidParameterTest4() {
        String[] args = {"--token", "token", "files", "updates", "--file", "fileid",
        "name=file1", "metadata.key1=value1"};
        Cgccli.parseArgumentsAndCallAPI(args, mockCgccliAPICaller);
    }
    
    @Test(expected = InvalidParameterException.class)
    public void downloadFileInvalidParameterTest1() {
        String[] args = {"--token", "token", "files", "download", "--file", "fileid",
        "--dest", "destination1", "destination2"};
        Cgccli.parseArgumentsAndCallAPI(args, mockCgccliAPICaller);
    }
    
    @Test(expected = InvalidParameterException.class)
    public void downloadFileInvalidParameterTest2() {
        String[] args = {"--token", "token", "files", "download", "--file", "fileid"};
        Cgccli.parseArgumentsAndCallAPI(args, mockCgccliAPICaller);
    }
}