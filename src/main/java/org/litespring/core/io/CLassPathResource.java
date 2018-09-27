package org.litespring.core.io;

import org.litespring.beans.util.ClassUtils;
import org.litespring.core.io.Resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class CLassPathResource implements Resource {
    private String path;

    private ClassLoader classLoader;

    public CLassPathResource(String path) {
        this(path, null);
    }

    public CLassPathResource(String path, ClassLoader classLoader) {
        Assert.notNull(path, "Path must not be null");
        this.path = path;
        this.classLoader = classLoader == null ? ClassUtils.getDefaultClassLoader() : classLoader;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        InputStream is = classLoader.getResourceAsStream(path);
        if (is == null) {
            throw new FileNotFoundException(path + " cannot be opened");
        }
        return is;
    }

    @Override
    public String getDescription() {
        return path;
    }
}
