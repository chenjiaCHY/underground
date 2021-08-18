package com.ntschy.underground;

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
	}

//	@Test
//	public void testEncrypt() {
//		BasicTextEncryptor encryptor = new BasicTextEncryptor();
//		encryptor.setPassword("NTSchy123456");
//		System.out.println(encryptor.encrypt("sde"));
//	}

}
