package coupon.global;


import com.zaxxer.hikari.HikariConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "coupon.datasource")
public class CouponDatasourceProperties {

    private HikariConfig writer;
    private HikariConfig reader;
}
