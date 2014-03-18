package logic;

import java.util.List;
import java.util.Map;

public interface LogicService {

	public String handleCommand(String command, Map<String, List<String>> map);
}
