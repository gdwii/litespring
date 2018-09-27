package org.litespring.test.v1;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.core.io.CLassPathResource;
import org.litespring.core.io.FileSystemResource;
import org.litespring.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

public class ResourceTest {
    @Test
    public void testClassPathResource() throws IOException {
        Resource resource = new CLassPathResource("petstore-v1.xml");
        try(InputStream in = resource.getInputStream()){
            Assert.assertNotNull(in);
        }
    }

    @Test
    public void testFileSystemResource() throws IOException {
        String path = ResourceTest.class.getClassLoader().getResource("petstore-v1.xml").getPath();
        Resource resource = new FileSystemResource(path);
        try(InputStream in = resource.getInputStream()){
            Assert.assertNotNull(in);
        }
    }
}