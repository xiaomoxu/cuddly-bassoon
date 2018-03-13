package com.bassoon.stockextractor.config;

import org.python.util.PythonInterpreter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class JythonConfig {

    @Bean
    public PythonInterpreter pythonInterpreter() {
        Properties props = new Properties();
        props.put("python.home", "../jython-2.7.0");
        props.put("python.console.encoding", "UTF-8");
        props.put("python.security.respectJavaAccessibility", "false");
        props.put("python.import.site", "false");
        Properties preprops = System.getProperties();
        PythonInterpreter.initialize(preprops, props, new String[0]);
        PythonInterpreter pyInterpreter = new PythonInterpreter();
        pyInterpreter.exec("import sys");
        pyInterpreter.exec("print 'prefix', sys.prefix");
        pyInterpreter.exec("print sys.path");
        System.out.println("python的jar包引用正确");
        return pyInterpreter;
    }

}
