package edu.xidian.pnaWeb.petri.alg;

import edu.xidian.pnaWeb.petri.module.PetriDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Description
 * @Author He
 * @Date 2021/12/5 20:25
 */
@Component
@Slf4j
public class PythonAlg {
	public String getInvariantP(String postMatrix,String preMatrix) {
		try {
			StringBuilder executeAns=new StringBuilder();
			log.info(postMatrix);
			log.info(preMatrix);
			String[] args = new String[] { "python", "E:\\codes\\study\\pythonProj\\invariantP.py", postMatrix,preMatrix };
			Process proc = Runtime.getRuntime().exec(args);// 执行py文件
			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = null;
			while ((line = in.readLine()) != null) {
				executeAns.append(line).append("\n");
			}
			in.close();
			proc.waitFor();
			log.info(executeAns.toString());
			return executeAns.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "error";
	}
}
