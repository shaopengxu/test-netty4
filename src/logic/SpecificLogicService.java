package logic;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class SpecificLogicService implements LogicService {

	@Override
	public String handleCommand(String command, Map<String, List<String>> map) {
		try {
			Method method = getClass().getDeclaredMethod(command, Map.class);
			return (String) method.invoke(this, map);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public String getUser(Map<String, List<String>> map) {
		return "haha";
	}

	private static LogicService logicService = new SpecificLogicService();

	public static LogicService getInstance() {
		return logicService;
	}

}
