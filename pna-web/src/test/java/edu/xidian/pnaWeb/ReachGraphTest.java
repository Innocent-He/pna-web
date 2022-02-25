package edu.xidian.pnaWeb;

import edu.xidian.pnaWeb.petri.alg.AcNetAlg;
import edu.xidian.pnaWeb.petri.alg.PetriNetAlg;
import edu.xidian.pnaWeb.petri.alg.ReachableGraphAlg;
import edu.xidian.pnaWeb.web.model.PetriDTO;
import org.junit.Test;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * @Description
 * @Author He
 * @Date 2021/10/16 19:49
 */

public class ReachGraphTest {
	@Resource
	private ReachableGraphAlg reachableGraph;

	private String input="{\"nodeList\":[{\"type\":\"place\",\"nodeName\":\"库所\",\"token\":\"4\",\"id\":\"p-1\",\"height\":50,\"y\":3250,\"x\":3250},{\"type\":\"transaction\",\"nodeName\":\"变迁\",\"id\":\"t-1\",\"height\":50,\"y\":3025,\"x\":3465},{\"type\":\"place\",\"nodeName\":\"库所\",\"token\":0,\"id\":\"p-2\",\"height\":50,\"y\":3100,\"x\":3390},{\"type\":\"place\",\"nodeName\":\"库所\",\"token\":0,\"id\":\"p-3\",\"height\":50,\"y\":3100,\"x\":3525},{\"type\":\"place\",\"nodeName\":\"库所\",\"token\":0,\"id\":\"p-4\",\"height\":50,\"y\":3305,\"x\":3405},{\"type\":\"place\",\"nodeName\":\"库所\",\"token\":0,\"id\":\"p-5\",\"height\":50,\"y\":3300,\"x\":3535},{\"type\":\"place\",\"nodeName\":\"库所\",\"token\":\"4\",\"id\":\"p-6\",\"height\":50,\"y\":3245,\"x\":3965},{\"type\":\"place\",\"nodeName\":\"库所\",\"token\":0,\"id\":\"p-7\",\"height\":50,\"y\":3430,\"x\":3840},{\"type\":\"place\",\"nodeName\":\"库所\",\"token\":0,\"id\":\"p-8\",\"height\":50,\"y\":3285,\"x\":3835},{\"type\":\"place\",\"nodeName\":\"库所\",\"token\":0,\"id\":\"p-9\",\"height\":50,\"y\":3140,\"x\":3835},{\"type\":\"place\",\"nodeName\":\"库所\",\"token\":0,\"id\":\"p-10\",\"height\":50,\"y\":3000,\"x\":3835},{\"type\":\"place\",\"nodeName\":\"库所\",\"token\":\"1\",\"id\":\"p-11\",\"height\":50,\"y\":3040,\"x\":3680},{\"type\":\"place\",\"nodeName\":\"库所\",\"token\":\"1\",\"id\":\"p-12\",\"height\":50,\"y\":3155,\"x\":3680},{\"type\":\"place\",\"nodeName\":\"库所\",\"token\":\"1\",\"id\":\"p-13\",\"height\":50,\"y\":3265,\"x\":3680},{\"type\":\"place\",\"nodeName\":\"库所\",\"token\":\"1\",\"id\":\"p-14\",\"height\":50,\"y\":3390,\"x\":3680},{\"type\":\"transaction\",\"nodeName\":\"变迁\",\"id\":\"t-2\",\"height\":50,\"y\":3220,\"x\":3395},{\"type\":\"transaction\",\"nodeName\":\"变迁\",\"id\":\"t-3\",\"height\":50,\"y\":3225,\"x\":3535},{\"type\":\"transaction\",\"nodeName\":\"变迁\",\"id\":\"t-4\",\"height\":50,\"y\":3425,\"x\":3480},{\"type\":\"transaction\",\"nodeName\":\"变迁\",\"id\":\"t-5\",\"height\":50,\"y\":3520,\"x\":3840},{\"type\":\"transaction\",\"nodeName\":\"变迁\",\"id\":\"t-6\",\"height\":50,\"y\":3365,\"x\":3840},{\"type\":\"transaction\",\"nodeName\":\"变迁\",\"id\":\"t-7\",\"height\":50,\"y\":3225,\"x\":3845},{\"type\":\"transaction\",\"nodeName\":\"变迁\",\"id\":\"t-8\",\"height\":50,\"y\":3080,\"x\":3840},{\"type\":\"transaction\",\"nodeName\":\"变迁\",\"id\":\"t-9\",\"height\":50,\"y\":2940,\"x\":3835}],\"linkList\":[{\"type\":\"link\",\"id\":\"link-4a0a9919db90463f94f2cfa46676dd2b\",\"sourceId\":\"p-1\",\"targetId\":\"t-1\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-f920f06c3ab04a4d84d3eb22cc992277\",\"sourceId\":\"t-1\",\"targetId\":\"p-2\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-8af3b4391e8444fabb32e40f7e085087\",\"sourceId\":\"t-1\",\"targetId\":\"p-3\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-63354454138e4a46ba7f8ba8087e8808\",\"sourceId\":\"p-2\",\"targetId\":\"t-2\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-4e9a384ed30c46a29b5c0b05a87fe0dc\",\"sourceId\":\"p-3\",\"targetId\":\"t-3\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-1c0b79190b954fdd82ce2831231a6c6b\",\"sourceId\":\"t-2\",\"targetId\":\"p-4\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-57bb4a25229c45a18b19bbf99d407302\",\"sourceId\":\"t-3\",\"targetId\":\"p-5\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-52a92bfcfca14bcc87213c5b93ad2ff2\",\"sourceId\":\"p-4\",\"targetId\":\"t-4\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-c194b420aa2e4693809928d4bed1e62e\",\"sourceId\":\"p-5\",\"targetId\":\"t-4\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-82ca6161696044acb118b6fee88f1d3a\",\"sourceId\":\"t-4\",\"targetId\":\"p-1\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-ab2f79578d2341e688a06f2fa6b89cb6\",\"sourceId\":\"t-4\",\"targetId\":\"p-14\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-baadd5136835483a8be3eea3d9afc8fe\",\"sourceId\":\"p-14\",\"targetId\":\"t-5\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-4b74cf54b43b4772b50a08df8604fde1\",\"sourceId\":\"p-6\",\"targetId\":\"t-5\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-fd7bd4076db54be2892e425c1899a5bc\",\"sourceId\":\"t-9\",\"targetId\":\"p-6\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-3b88ba370d2d4b738b9997a689af40b7\",\"sourceId\":\"t-5\",\"targetId\":\"p-7\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-6d8055563cec4a74810ab498dc6d34e0\",\"sourceId\":\"p-7\",\"targetId\":\"t-6\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-ea067b8883d241af953cf19cf65181cb\",\"sourceId\":\"t-6\",\"targetId\":\"p-8\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-42dc9692e2dc4afb9f93e203a2c88331\",\"sourceId\":\"p-8\",\"targetId\":\"t-7\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-b506fa9794a44c2d8c855ef44a758fe1\",\"sourceId\":\"t-7\",\"targetId\":\"p-9\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-dfbc3df8bfc8498abbda722f8e260af8\",\"sourceId\":\"p-9\",\"targetId\":\"t-8\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-9c805d8e983b4f439e46260637cc536e\",\"sourceId\":\"t-8\",\"targetId\":\"p-10\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-47839efd5d10409d903dd88b959aba10\",\"sourceId\":\"p-10\",\"targetId\":\"t-9\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-4306d6447f9043c5ac4a917b997e3da8\",\"sourceId\":\"t-9\",\"targetId\":\"p-11\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-ee6861ac30124f528eddfaa6f9510314\",\"sourceId\":\"p-11\",\"targetId\":\"t-8\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-f21def6bf4464e26958ea316ac9379c6\",\"sourceId\":\"p-11\",\"targetId\":\"t-1\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-cd66c03415794c7289b9b6526714084e\",\"sourceId\":\"t-2\",\"targetId\":\"p-11\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-8a6badadd6c74f7ba9c7fedb05095985\",\"sourceId\":\"p-12\",\"targetId\":\"t-1\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-ff4555b55637467b8b77bdf5d86dbb39\",\"sourceId\":\"t-8\",\"targetId\":\"p-12\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-42af315e3ddc44728ca2466f03855ad2\",\"sourceId\":\"p-12\",\"targetId\":\"t-7\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-0b6d48ed820742ba884265d6a862497d\",\"sourceId\":\"t-3\",\"targetId\":\"p-12\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-b67cb717e93d4eb488e74974e02c1b2d\",\"sourceId\":\"t-7\",\"targetId\":\"p-13\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-ce2382f5db754c8c986e0ca8fab3d4f2\",\"sourceId\":\"p-13\",\"targetId\":\"t-6\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-ead814b6d17940a58a3f00ef6b3b6c0c\",\"sourceId\":\"t-4\",\"targetId\":\"p-13\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-be40cc198219460080897adb54849998\",\"sourceId\":\"p-13\",\"targetId\":\"t-3\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-a8763cde90804a6da51b46fc77b4637c\",\"sourceId\":\"p-14\",\"targetId\":\"t-2\",\"weight\":\"\"},{\"type\":\"link\",\"id\":\"link-da72fc6ff54140bf84b3498539ac70a6\",\"sourceId\":\"t-6\",\"targetId\":\"p-14\",\"weight\":\"\"}],\"attr\":{\"name\":\"flow-1ad5a37959a045ab872f41a8c8601aae\",\"des\":\"\",\"createTime\":\"\",\"maxPlaceId\":14,\"maxTranId\":9},\"status\":\"1\"}";
	@Test
	public void test1() {
		PetriNetAlg petriNetAlg = new AcNetAlg();
	}
	@Test
	public void test2() {

		int nThreads = Runtime.getRuntime().availableProcessors();
	}
	@Test
	public void validJudge() {
		String jsonPost="[[0,0,1,0,0,0,0,0,1],[1,0,0,0,0,0,0,0,0],[0,1,0,0,0,0,0,0,0],[0,0,0,0,0,1,0,0,0],[0,0,0,1,0,0,0,0,0],[0,0,0,0,1,0,0,0,0],[0,1,0,0,0,1,0,0,0],[0,0,1,0,1,0,0,0,0],[0,0,0,0,0,0,1,0,0],[0,0,0,0,0,0,0,1,0],[0,0,0,0,0,0,0,0,1],[1,0,0,0,0,0,0,1,0]]";
		String jsonPre="[[0,0,0,0,0,0,1,0,0],[0,1,0,0,0,0,0,0,0],[0,0,1,0,0,0,0,0,0],[0,0,0,1,0,0,0,0,0],[0,0,0,0,1,0,0,0,0],[0,0,0,0,0,1,0,0,0],[1,0,0,0,1,0,0,0,0],[0,1,0,1,0,0,0,0,0],[1,0,0,0,0,0,0,1,0],[0,0,0,0,0,0,0,0,1],[0,0,0,0,0,0,0,1,0],[0,0,0,0,0,0,1,0,0]]";
		try {
			String[] args = new String[] { "python", "E:\\codes\\study\\pythonProj\\invariantP.py", jsonPost,jsonPre };
			Process proc = Runtime.getRuntime().exec(args);// 执行py文件
			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = null;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
			in.close();
			proc.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void test5() {
		List<Integer> tranIds=IntStream.range(0,5).collect(
				ArrayList::new,
				ArrayList::add,
				ArrayList::addAll);
		System.out.println(tranIds);
	}

	Map<Integer,List<Integer>> dependTrans=new HashMap<>();
	Map<Integer,List<Integer>> placePostTrans=new HashMap<>();
	Map<Integer,List<Integer>>tranPostPlaces=new HashMap<>();
	List<List<Integer>> result=new ArrayList<>();
	public List<Integer> findDependTrans(Integer id,boolean isPlace) {
		if (isPlace) {
			return placePostTrans.get(id);
		}
		List<List<Integer>> allTrans=new ArrayList<>();
		for (Integer placeId : tranPostPlaces.get(id)) {
			List<Integer> dependTrans = findDependTrans(placeId, true);
			allTrans.add(dependTrans);
		}
		return null;
	}
}
