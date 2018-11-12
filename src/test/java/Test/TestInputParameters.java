/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import com.test.cgccli.Cgccli;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Luka Potkonjak
 */
public class TestInputParameters {
    private MockCgccliAPICaller mockCgccliAPICaller;
    
    @Before
    public void setUp() {
        mockCgccliAPICaller = new MockCgccliAPICaller();
    }
    
    @Test
    public void getProjectsParameterTest() {
        String[] args = {"--token", "token", "projects", "list"};
        Cgccli.parseArgumentsAndCallAPI(args, mockCgccliAPICaller);
        Assert.assertEquals(1, mockCgccliAPICaller.getGetProjectsCounter());
    }
    
    @Test
    public void getFilesParameterTest() {
        String[] args = {"--token", "token1", "files", "list", "--project", "user1/projectid1"};
        Cgccli.parseArgumentsAndCallAPI(args, mockCgccliAPICaller);
        args = new String[] {"--token", "token2", "files", "list", "--project", "user2/projectid2"};
        Cgccli.parseArgumentsAndCallAPI(args, mockCgccliAPICaller);
        Assert.assertEquals(2, mockCgccliAPICaller.getGetFilesCounter());
    }
    
    @Test
    public void updateFileParameterTest() {
        String[] args = {"--token", "token", "files", "update", "--file", "fileid",
            "name=file1", "tags=tag1,tag2"};
        Cgccli.parseArgumentsAndCallAPI(args, mockCgccliAPICaller);
        args = new String[]{"--token", "token", "files", "update", "--file", "fileid",
            "metadata.name1=value1", "metadata.name1=value2", "tags=tag1,tag2"};
        Cgccli.parseArgumentsAndCallAPI(args, mockCgccliAPICaller);
        args = new String[]{"--token", "token", "files", "update", "--file", "fileid",
            "tags=tag1,tag2,tag3,tag4", "metadata.name1=value1", "name=file1"};
        Cgccli.parseArgumentsAndCallAPI(args, mockCgccliAPICaller);
        args = new String[]{"--token", "token", "files", "update", "--file", "fileid",
            "name=file1", "metadata.name1=value1"};
        Cgccli.parseArgumentsAndCallAPI(args, mockCgccliAPICaller);
        Assert.assertEquals(4, mockCgccliAPICaller.getUpdateFileDetailsCounter());
    }

    @Test
    public void downloadFileParameterTest() {
        String[] args = {"--token", "token1", "files", "download", "--file", "fileid1",
            "--dest", "destination1"};
        Cgccli.parseArgumentsAndCallAPI(args, mockCgccliAPICaller);
        args = new String[]{"--token", "token2", "files", "download", "--file", "fileid2",
            "--dest", "destination2"};
        Cgccli.parseArgumentsAndCallAPI(args, mockCgccliAPICaller);
        Assert.assertEquals(2, mockCgccliAPICaller.getGetDownloadCounter());
    }
}
