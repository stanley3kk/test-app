package kr.co.uplus.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EntityScan(basePackages = ["kr.co.uplus.app"])
@EnableJpaRepositories(basePackages = ["kr.co.uplus.app"])

class AppApplication

fun main(args: Array<String>) {
	runApplication<AppApplication>(*args)
}
