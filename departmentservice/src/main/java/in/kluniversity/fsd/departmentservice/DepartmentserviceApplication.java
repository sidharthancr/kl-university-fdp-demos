package in.kluniversity.fsd.departmentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DepartmentserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DepartmentserviceApplication.class, args);
	}

}
