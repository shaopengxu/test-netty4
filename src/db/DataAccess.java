package db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * 
 */
public class DataAccess {

	private static DataAccess dataAccess = new DataAccess();

	private String topPath = "data";
	public static String slash = "/";
	public static int mapNumber = 1000;
	public static String underline = "_";
	public static String fileSuffix = ".obj";
	public static Map<String, Object> fileLockMap = new ConcurrentHashMap<String, Object>();
	public static Map<String, Object[]> objectMap = new ConcurrentHashMap<String, Object[]>();
	public static int timeOutHour = 2;

	public static DataAccess getInstance() {
		return dataAccess;
	}

	public String getObjectKey(String className, int id) {
		return new StringBuilder().append(className).append(id).toString();
	}

	public synchronized Object getFileLockObject(String s) {
		Object obj = fileLockMap.get(s);
		if (obj == null) {
			obj = new Object();
			fileLockMap.put(s, obj);
		}
		return obj;
	}

	public void checkDirExist(String className, int id) {
		StringBuilder fileNameSb = new StringBuilder();
		fileNameSb.append(topPath);
		File file = new File(fileNameSb.toString());
		if (!file.exists()) {
			file.mkdir();
		}
		fileNameSb.append(slash).append(className);
		file = new File(fileNameSb.toString());
		if (!file.exists()) {
			file.mkdir();
		}
		fileNameSb.append(slash).append(id / mapNumber);
		file = new File(fileNameSb.toString());
		if (!file.exists()) {
			file.mkdir();
		}
	}

	public String getObjectFilePath(String className, int id) {
		return new StringBuilder().append(topPath).append(slash)
				.append(className).append(slash).append(id / mapNumber)
				.toString();
	}

	public String getObjectFileName(String className, int id) {
		return new StringBuilder().append(topPath).append(slash)
				.append(className).append(slash).append(id / mapNumber)
				.append(slash).append(className).append(underline).append(id)
				.append(fileSuffix).toString();
	}

	public void writeObject(Object o, int id) {
		String fileName = getObjectFileName(o.getClass().getSimpleName(), id);
		checkDirExist(o.getClass().getSimpleName(), id);
		try {
			String s = getObjectKey(o.getClass().getSimpleName(), id);
			Object lock = getFileLockObject(s);
			synchronized (lock) {
				ObjectOutputStream out = new ObjectOutputStream(
						new FileOutputStream(fileName));
				out.writeObject(o);
				out.flush();
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Object readObject(Class clazz, int id) {
		String fileName = getObjectFileName(clazz.getSimpleName(), id);
		checkDirExist(clazz.getSimpleName(), id);
		try {
			String s = getObjectKey(clazz.getSimpleName(), id);
			if (objectMap.get(s) != null) {
				objectMap.get(s)[0] = System.currentTimeMillis();
				return objectMap.get(s)[1];
			}
			Object lock = getFileLockObject(s);
			synchronized (lock) {
				ObjectInputStream in = new ObjectInputStream(
						new FileInputStream(fileName));
				Object object = in.readObject();
				in.close();
				Object[] objs = new Object[2];
				objs[0] = System.currentTimeMillis();
				objs[1] = object;
				objectMap.put(s, objs);
				return object;
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 每两个小时执行一次
	 */
	public void clearTimeOutObject() {
		for (Iterator<String> iterator = objectMap.keySet().iterator(); iterator
				.hasNext();) {
			String s = iterator.next();
			Long time = (Long) objectMap.get(s)[0];
			if (System.currentTimeMillis() - time > timeOutHour * 60 * 60 * 1000) {
				iterator.remove();
			}
		}
	}

	public static void main(String[] args) {
		TestObject to = new TestObject();
		DataAccess da = getInstance();
		long time = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			da.writeObject(to, i);
			to = (TestObject) da.readObject(TestObject.class, i);
		}

		System.out.println(System.currentTimeMillis() - time);
	}

}

class TestObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int i = 3;
	int j = 4;
}
