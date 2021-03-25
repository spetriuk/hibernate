package com.petriuk.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Validator {
    private static final Logger LOG = LogManager.getLogger();
    private static final Properties props = new Properties();
    private static final String PROPERTIES_FILE = "validation.properties";

    static {
        try (InputStream inputStream = Validator.class
            .getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (inputStream == null) {
                throw new IOException(PROPERTIES_FILE + " file not found");
            }
            props.load(inputStream);
        } catch (IOException e) {
            LOG.error(e);
            throw new RuntimeException(e);
        }
    }

    public static Map<String, String> getErrors(Map<String, String> fields,
        boolean checkPassword) {
        Map<String, String> errors = new HashMap<>();
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            if (!checkPassword && (entry.getKey().equals("password") ||
                entry.getKey().equals("password2"))) {
                continue;
            }
            if (entry.getValue() == null || entry.getValue().isEmpty()) {
                errors.put(entry.getKey(), props.getProperty("message.empty"));
                continue;
            }
            if (fieldNotValidByRegex(entry.getValue(), entry.getKey())) {
                errors.put(entry.getKey(),
                    props.getProperty("message." + entry.getKey()));
            }
        }
        if (checkPassword && !errors.containsKey("password") && !fields
            .get("password").equals(fields.get("password2"))) {
            errors.put("password", props.getProperty("error.pass.check"));
            errors.put("password2", props.getProperty("error.pass.check"));
        }
        return errors;
    }

    private static boolean fieldNotValidByRegex(String value, String key) {
        String regexp = props.getProperty("regexp." + key);
        return regexp != null && !value.matches(regexp);
    }

}
