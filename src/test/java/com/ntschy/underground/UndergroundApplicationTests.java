package com.ntschy.underground;

import com.ntschy.underground.utils.Utils;
import org.jasypt.util.text.BasicTextEncryptor;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
@SpringBootTest
class UndergroundApplicationTests {

	@Test
	void contextLoads() {
		for (int i = 0; i < 10; i ++) {
			System.out.println(Utils.GenerateUUID(32));
		}
	}

}
