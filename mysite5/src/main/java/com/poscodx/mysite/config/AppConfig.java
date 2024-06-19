package com.poscodx.mysite.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.poscodx.mysite.config.app.DBConfig;
import com.poscodx.mysite.config.app.MyBatisConfig;

@Configuration
@EnableAspectJAutoProxy
@EnableTransactionManagement
@ComponentScan({"com.poscodx.mysite.service", "com.poscodx.mysite.repository", "com.poscodx.mysite.aspect"})
@Import({DBConfig.class, MyBatisConfig.class})
public class AppConfig {
}