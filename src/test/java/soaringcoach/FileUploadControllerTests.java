package soaringcoach;

import java.io.FileInputStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import soaringcoach.rest.Application;


/**
 * Created by russel on 15/12/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class FileUploadControllerTests  {

    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testFileUpload() throws Exception{
        Resource fileSystemResource = new ClassPathResource("5c5vjf21.igc");
        //File is correctly loaded
        if(fileSystemResource.exists()){
            final MockMultipartFile multipartFile = new MockMultipartFile(fileSystemResource.getFilename(), new FileInputStream(fileSystemResource.getFile()));

            System.out.println("We got the file!!!!"+fileSystemResource.getFilename());
            /*mockMvc.perform(
                    post("/file-upload")
                            .requestAttr("file", multipartFile.getBytes())
                            .requestAttr("something", ":(")
                            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                    .andExpect(status().isCreated());*/
        }else{
            System.out.println("this file doesn't exist");
        }

    }

}