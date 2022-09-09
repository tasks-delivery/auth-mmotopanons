package auth.settings;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(name = "ApplicationProperties", value = "application.properties")
public class CommonSettings {

    @Value("#{'${admins}'.split(',')}")
    private String[] admins;

}
