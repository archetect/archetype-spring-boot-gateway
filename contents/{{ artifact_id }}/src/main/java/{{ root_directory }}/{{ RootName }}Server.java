package {{ root_package }};

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class {{ RootName }}Server {

    private static final String SPRING_CONFIG_NAME = "spring.config.name";
    private static final String SPRING_APPLICATION_NAME = "spring.application.name";
    private static final String SPRING_JMX_DEFAULT = "spring.jmx.default";
    private static final String SPRING_JMX_ENABLED = "spring.jmx.enabled";

    private final SpringApplication springApplication;
    private ConfigurableApplicationContext context;
    private final List<String> arguments = new ArrayList<>();
    private String applicationName;
    private boolean disableJmx = false;

    public {{ RootName }}Server(String applicationName, Class<?>... primarySources) {
        this.applicationName = applicationName;
        this.springApplication = new SpringApplication(primarySources);
    }

    public {{ RootName }}Server() {
        this("{{ artifact_id }}");
    }

    public {{ RootName }}Server(String applicationName) {
        this(applicationName, {{ RootName }}Config.class);
    }

    public void start() {
        initializeSystemProperties();
        String[] args = new String[arguments.size()];
        args = arguments.toArray(args);
        this.context = springApplication.run(args);
        clearSystemProperties();
    }

    public void stop() {
        context.close();
    }

    private void initializeSystemProperties() {
        System.setProperty(SPRING_CONFIG_NAME, applicationName);
        System.setProperty(SPRING_APPLICATION_NAME, applicationName);
        System.setProperty(SPRING_JMX_DEFAULT, applicationName);
        if (disableJmx) {
            System.setProperty(SPRING_JMX_ENABLED, "false");
        }
    }

    private void clearSystemProperties() {
        System.clearProperty(SPRING_CONFIG_NAME);
        System.clearProperty(SPRING_APPLICATION_NAME);
        System.clearProperty(SPRING_JMX_DEFAULT);
        if (disableJmx) {
            System.clearProperty(SPRING_JMX_ENABLED);
        }
    }

    public {{ RootName }}Server disableJmx() {
        this.disableJmx = true;
        return this;
    }

    public {{ RootName }}Server withArguments(String... args) {
        arguments.addAll(Arrays.asList(args));
        return this;
    }

    public Optional<ApplicationContext> getContext() {
        return Optional.ofNullable(context);
    }

    public static void main(String[] args) {
        new {{ RootName }}Server().withArguments(args).start();
    }
}
