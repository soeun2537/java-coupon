package coupon.global;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class DataSourceConfig {

    private final CouponDatasourceProperties properties;

    @Bean
    @Primary
    public DataSource routingDataSource() {
        DataSource writer = new HikariDataSource(properties.getWriter());
        DataSource reader = new HikariDataSource(properties.getReader());

        Map<Object, Object> targetDataSource = new HashMap<>();
        targetDataSource.put(DataSourceType.WRITER, writer);
        targetDataSource.put(DataSourceType.READER, reader);

        ReadOnlyDataSourceRouter dataSource = new ReadOnlyDataSourceRouter();
        dataSource.setDefaultTargetDataSource(writer);
        dataSource.setTargetDataSources(targetDataSource);
        dataSource.afterPropertiesSet();
        return new LazyConnectionDataSourceProxy(dataSource);
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(routingDataSource())
                .packages("coupon")
                .build();
    }

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
