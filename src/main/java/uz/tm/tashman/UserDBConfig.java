//package uz.tm.tashman;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class UserDBConfig {
//
//    //Primary DataSource in Postgresql
//    @Bean(name = "dsMaster")
//    @ConfigurationProperties("spring.datasource")
//    @Primary
//    public DataSource primaryDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean(name = "jdbcMaster")
//    @Primary
//    @Autowired
//    public JdbcTemplate mainJdbcTemplate(@Qualifier("dsMaster") DataSource dsMaster){
//        return new JdbcTemplate(dsMaster);
//    }
//
//}
