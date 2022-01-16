package edu.xidian.pnaWeb;

import com.alibaba.fastjson.JSON;
import edu.xidian.pnaWeb.module.User;
import edu.xidian.pnaWeb.web.model.PetriDataDTO;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PnaWebApplicationTests {

	public void contextLoads() {
		String str = "{\"nodeList\":[{\"type\":\"place-node-normal\",\"nodeName\":\"普通库所\",\"id\":\"p-1\",\"height\":50,\"y\":3095,\"x\":3160},{\"type\":\"transaction\",\"nodeName\":\"变迁\",\"id\":\"t-1\",\"height\":50,\"y\":3165,\"x\":3370},{\"type\":\"place-node-free\",\"nodeName\":\"闲置库所\",\"id\":\"p-2\",\"height\":50,\"y\":3275,\"x\":3180},{\"type\":\"transaction\",\"nodeName\":\"变迁\",\"id\":\"t-2\",\"height\":50,\"y\":3200,\"x\":3060}],\"linkList\":[{\"type\":\"link\",\"id\":\"link-2a5d4caa973a444dbb3d293664aedce2\",\"sourceId\":\"p-1\",\"targetId\":\"t-1\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-a435ca1aebb34076891b5f6c3976c64e\",\"sourceId\":\"t-1\",\"targetId\":\"p-2\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-8f2360acaeb841928bd2b34b5f689ea6\",\"sourceId\":\"p-2\",\"targetId\":\"t-2\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-de67a0d5512947c4aa0915ac52a42121\",\"sourceId\":\"t-2\",\"targetId\":\"p-1\",\"weight\":\"\"}],\"attr\":{\"name\":\"flow-dd89359d610f4fca86079f27c4f6cb1c\",\"des\":\"\",\"createTime\":\"\"},\"status\":\"1\"}";
		PetriDataDTO petriDataDTO = JSON.parseObject(str, PetriDataDTO.class);
		String user = "{\"name\":\"asda\",\"age\":19}";
		User user1 = JSON.parseObject(user, User.class);
		System.out.println(user1);
		System.out.println(petriDataDTO);
	}

}
