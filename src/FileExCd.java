import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class FileExCd {
	static String[] argArr;
	static File curDir;

	public static void main(String[] args) {
		curDir = new File(System.getProperty("user.dir"));
		Scanner s = new Scanner(System.in);
		String input = "", prompt = "";

		while (true) {
			try {
				prompt = curDir.getCanonicalPath() + ">";
				System.out.print(prompt);
				input = s.nextLine();

				input = input.trim();
				argArr = input.split(" ");

				String command = argArr[0].trim();
				if (command.startsWith("cd") || command.startsWith("CD")) {
					cd();
				} else if (command.equalsIgnoreCase("dir")) {
					dir();
				} else if (command.equalsIgnoreCase("type")) {
					type();
				} else if (command.equalsIgnoreCase("copy")) {
					copy();
				} else if (command.equalsIgnoreCase("md")) {
					md();
				} else if (command.equalsIgnoreCase("exit")) {
					exit();
				} else if (command.equalsIgnoreCase("del")) {
					del();
				} else if (command.equalsIgnoreCase("rd")) {
					rd();
				} else if (command.equalsIgnoreCase("deltree")) {
					deltree();
				} else {
					for (int i = 0; i < argArr.length; i++) {
						System.out.println(argArr[i]);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// md - Make Diractory

	public static void md() throws IOException {
		String inputPath = argArr[1];
		if (!(argArr[1].indexOf(":") > -1)) {
			inputPath = curDir.getCanonicalPath() + "\\" + argArr[1];
		}
		File tmp = new File(inputPath);
		File mkdir = new File(inputPath);
		if (mkdir.exists()) {
			System.out.println("���� ���͸� �Ǵ� ���� " + mkdir.getName() + "��(��) �̹� �ֽ��ϴ�.");
		}
		if (!mkdir.mkdirs()) {
			System.out.println("��� ������ �ùٸ��� �ʽ��ϴ�.");
			return;
		}
	}

	// exit - exit

	public static void exit() {
		System.exit(0);
	}

	// del - Delete File

	public static void del() throws IOException {
		String inputPath = curDir.getCanonicalPath() + "\\" + argArr[1];
		Scanner sc = new Scanner(System.in);
		String chk;
		if (!(argArr[1].indexOf(":") > -1)) {
			inputPath = curDir.getCanonicalPath() + "\\" + argArr[1];
		}
		File mkdir = new File(inputPath);
		File[] list;
		if (!mkdir.exists()) {
			System.out.println(mkdir.getCanonicalPath() + "��(��) ã�� �� �����ϴ�.");
			return;
		}
		if (mkdir.isDirectory()) {
			list = mkdir.listFiles();
			while (true) {
				System.out.print(mkdir.getCanonicalPath() + "\\*, ����Ͻðڽ��ϱ�(Y/N)?");
				chk = sc.nextLine();
				if (chk.equalsIgnoreCase("Y")) {
					for (File file : list) {
						if (file.isFile())
							file.delete();
					}
					return;
				} else if (chk.equalsIgnoreCase("N")) {
					return;
				}
			}
		}
		if (!mkdir.isDirectory()) {
			mkdir.delete();
		}
	}

	// rd - Delete Directory

	public static void rdContent(File file) {
		File[] list = file.listFiles();
		for (File tmp : list) {
			if (tmp.isDirectory()) {
				rdContent(tmp);
			}
			tmp.delete();
		}
	}
	
	//ren - Rename File
	
	public static void ren() {
		if(argArr.length == 3) {
			File orginFile = new File(argArr[1]);
			File newFile =  new File(argArr[2]);
		}else {
			System.out.println("��� ������ �ùٸ��� �ʽ��ϴ�.");
			return;
		}
	}
	
	//deltree - Delete All Directory
	
	public static void deltree() throws IOException
	{
		String inputPath = argArr[1];
		Scanner sc = new Scanner(System.in);
		String chk;
		if (!(argArr[1].indexOf(":") > -1)) {
			inputPath = curDir.getCanonicalPath() + "\\" + argArr[1];
		}
		File mkdir = new File(inputPath);
		if (!mkdir.exists()) {
			System.out.println("������ ������ ã�� �� �����ϴ�.");
			return;
		}
		rdContent(mkdir);
		mkdir.delete();
	}

	public static void rd() throws IOException {
		String inputPath = argArr[1];
		Scanner sc = new Scanner(System.in);
		String chk;
		if (!(argArr[1].indexOf(":") > -1)) {
			inputPath = curDir.getCanonicalPath() + "\\" + argArr[1];
		}
		File mkdir = new File(inputPath);
		if (!mkdir.exists()) {
			System.out.println("������ ������ ã�� �� �����ϴ�.");
			return;
		}
		File[] exitChk;
		exitChk = mkdir.listFiles();
		if (exitChk[0] == null) {
			mkdir.delete();
			return;
		} else {
			System.out.println("���͸��� ��� ���� �ʽ��ϴ�.");
			return;

		}
	}

	// Copy - Copy File

	public static void copy() throws IOException {

		File[] list = curDir.listFiles();
		int originCnt = 0, copyCnt = 0, i;
		String originPath, copyPath;
		File originFile, copyFile;
		if (argArr.length == 4 && argArr[1].equals("/y")) {
			originPath = argArr[2];
			copyPath = argArr[3];
			for (File file : list) {
				if (file.getName().equals(originPath)) {
					originCnt++;
				}
				if (file.getName().equals(copyPath)) {
					copyCnt++;
				}
			}
			originPath = curDir.getCanonicalPath() + "//" + originPath;
			copyPath = curDir.getCanonicalPath() + "//" + copyPath;
			if (originCnt == 0) {
				System.out.println("������ ������ ã�� �� �����ϴ�.");
			} else if (copyCnt == 1) {
				originFile = new File(originPath);
				copyFile = new File(copyPath);
				FileInputStream fis = new FileInputStream(originFile);
				FileOutputStream fos = new FileOutputStream(copyPath);

				try {
					while ((i = fis.read()) != -1) {

						// FileOutputStream�� ���� �����͸� ����Ѵ�.
						fos.write(i);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if (fis != null)
						fis.close();
					if (fos != null)
						fos.close();
				}
				System.out.println("�ٿ����Ⱑ �Ϸ�Ǿ����ϴ�");
			} else {
				originFile = new File(originPath);
				copyFile = new File(copyPath);
				FileInputStream fis = null;
				FileOutputStream fos = null;

				try {
					copyFile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				fis = new FileInputStream(originFile);
				fos = new FileOutputStream(copyPath);

				try {
					while ((i = fis.read()) != -1) {

						// FileOutputStream�� ���� �����͸� ����Ѵ�.
						fos.write(i);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if (fis != null)
						fis.close();
					if (fos != null)
						fos.close();
				}
				System.out.println("���簡 �Ϸ�Ǿ����ϴ�");
			}
		} else if (argArr.length == 3) {
			originPath = argArr[1];
			copyPath = argArr[2];
			for (File file : list) {
				if (file.getName().equals(originPath)) {
					originCnt++;
				}
				if (file.getName().equals(copyPath)) {
					copyCnt++;
				}
			}
			originPath = curDir.getCanonicalPath() + "//" + originPath;
			copyPath = curDir.getCanonicalPath() + "//" + copyPath;
			if (originCnt == 0) {
				System.out.println("������ ������ ã�� �� �����ϴ�.");
			} else if (copyCnt == 1) {
				Scanner sc = new Scanner(System.in);
				String input;
				originFile = new File(originPath);
				copyFile = new File(copyPath);
				FileInputStream fis = new FileInputStream(originFile);
				FileOutputStream fos = new FileOutputStream(copyPath);

				System.out.print(copyFile.getName() + "��(��) ����ðڽ��ϱ�? (Yes/No): ");
				input = sc.nextLine();

				if (input.equalsIgnoreCase("yes")) {
					try {
						while ((i = fis.read()) != -1) {

							// FileOutputStream�� ���� �����͸� ����Ѵ�.
							fos.write(i);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						if (fis != null)
							fis.close();
						if (fos != null)
							fos.close();
					}
					System.out.println("�ٿ����Ⱑ �Ϸ�Ǿ����ϴ�");
				} else if (input.equalsIgnoreCase("no")) {
					System.out.println("�ٿ����Ⱑ ��ҵǾ����ϴ�");
					return;
				} else {
					System.out.println("�߸��� �����Դϴ�.");
					return;
				}
			} else {
				originFile = new File(originPath);
				copyFile = new File(copyPath);
				FileInputStream fis = null;
				FileOutputStream fos = null;

				try {
					copyFile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				fis = new FileInputStream(originFile);
				fos = new FileOutputStream(copyPath);

				try {
					while ((i = fis.read()) != -1) {

						// FileOutputStream�� ���� �����͸� ����Ѵ�.
						fos.write(i);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if (fis != null)
						fis.close();
					if (fos != null)
						fos.close();
				}
				System.out.println("���簡 �Ϸ�Ǿ����ϴ�");
			}
		} else if (argArr.length == 4 && argArr[1].equals("/-y")) {
			originPath = argArr[2];
			copyPath = argArr[3];
			for (File file : list) {
				if (file.getName().equals(originPath)) {
					originCnt++;
				}
				if (file.getName().equals(copyPath)) {
					copyCnt++;
				}
			}
			originPath = curDir.getCanonicalPath() + "//" + originPath;
			copyPath = curDir.getCanonicalPath() + "//" + copyPath;
			if (originCnt == 0) {
				System.out.println("originPath : "+originPath+" copyPath : "+copyPath);
				System.out.println("������ ������ ã�� �� �����ϴ�.");
			} else if (copyCnt == 1) {
				Scanner sc = new Scanner(System.in);
				String input;
				originFile = new File(originPath);
				copyFile = new File(copyPath);
				FileInputStream fis = new FileInputStream(originFile);
				FileOutputStream fos = new FileOutputStream(copyPath);

				System.out.print(copyFile.getName() + "��(��) ����ðڽ��ϱ�? (Yes/No): ");
				input = sc.nextLine();

				if (input.equalsIgnoreCase("yes")) {
					try {
						while ((i = fis.read()) != -1) {

							// FileOutputStream�� ���� �����͸� ����Ѵ�.
							fos.write(i);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						if (fis != null)
							fis.close();
						if (fos != null)
							fos.close();
					}
					System.out.println("�ٿ����Ⱑ �Ϸ�Ǿ����ϴ�");
				} else if (input.equalsIgnoreCase("no")) {
					System.out.println("�ٿ����Ⱑ ��ҵǾ����ϴ�");
					return;
				} else {
					System.out.println("�߸��� �����Դϴ�.");
					return;
				}
			} else {
				originFile = new File(originPath);
				copyFile = new File(copyPath);
				FileInputStream fis = null;
				FileOutputStream fos = null;

				try {
					copyFile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				fis = new FileInputStream(originFile);
				fos = new FileOutputStream(copyPath);

				try {
					while ((i = fis.read()) != -1) {

						// FileOutputStream�� ���� �����͸� ����Ѵ�.
						fos.write(i);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if (fis != null)
						fis.close();
					if (fos != null)
						fos.close();
				}
				System.out.println("���簡 �Ϸ�Ǿ����ϴ�");
			}
		} else {
			System.out.println("�߸��� �����Դϴ�");
			return;
		}
	}

	// Type - Read File

	public static void type() throws IOException {
		File[] list = curDir.listFiles();
		String path = argArr[1];

		if (argArr.length == 2) {
			if (!(path.indexOf(":") > -1)) {
				path = curDir.getCanonicalPath() + "\\" + argArr[1];
			}
			File tmp = new File(path);
			if (tmp.exists()) {
				FileInputStream input = new FileInputStream(tmp);
				int i = 0;
				while ((i = input.read()) != -1) {
					System.out.write(i);
				}
			} else {
				System.out.println("������ ������ �������� �ʽ��ϴ�");
				return;
			}
			System.out.println();
		} else {
			System.out.println("��� ������ �ùٸ��� �ʽ��ϴ�.");
		}
	}

	// cd - Move Directory

	public static void cd() {
		if (argArr.length == 1 && !argArr[0].equalsIgnoreCase("cd..")) {
			System.out.println(curDir);
			return;
		} else if (argArr.length > 2) {
			System.out.println("�߸��� �����Դϴ�");
			return;
		} else if (argArr[0].equalsIgnoreCase("cd..") || argArr[1].equals("..")) {
			File newDir = curDir.getParentFile();

			if (newDir == null) {
				System.out.println("��ȿ���� ���� �����Դϴ�");
			} else {
				curDir = newDir;
			}
			return;
		} else if (argArr[1].equals(".")) {
			System.out.println(curDir);
			return;
		}
		String subDir = argArr[1];
		try {
			File cdFile = new File(curDir.getCanonicalFile() + "\\" + subDir);
			if (cdFile.isDirectory()) {
				curDir = cdFile;
			} else {
				System.out.println("��ȿ���� ���� �����Դϴ�");
				return;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Dir - File list

	public static void dir() {
		File[] list = curDir.listFiles();
		long millisec;

		if (argArr.length == 1) {
			for (File tempFile : list) {
				millisec = tempFile.lastModified();
				Date dt = new Date(millisec);
				SimpleDateFormat sd = new SimpleDateFormat("YYYY-MM-dd");

				System.out.print(sd.format(dt) + "\t");

				if (tempFile.isDirectory()) {
					System.out.print("<DIR>");
				}
				System.out.println("\t" + tempFile.getName());
			}
		} else if (argArr.length == 2 && argArr[1].equals("/ad")) {
			for (File tempFile : list) {
				millisec = tempFile.lastModified();
				Date dt = new Date(millisec);

				if (tempFile.isDirectory()) {
					SimpleDateFormat sd = new SimpleDateFormat("YYYY-MM-dd");
					System.out.print(sd.format(dt) + "\t");
					System.out.print("<DIR>");
					System.out.println("\t" + tempFile.getName());
				}
			}
		} else if (argArr.length == 2 && argArr[1].equals("/b")) {
			for (File tempFile : list) {
				System.out.println(tempFile.getName());
			}
		} else if (argArr.length == 2 && argArr[1].equals("/w")) {
			for (File tempFile : list) {
				System.out.print("[" + tempFile.getName() + "]\t");
			}
			System.out.println();
		} else if (argArr.length == 2) {
			for (File tempFile : list) {
				if (tempFile.isDirectory()) {
					if (tempFile.getName().equals(argArr[1])) {
						File selectedFile = new File(tempFile.getAbsolutePath());
						File[] selectedFilelist = selectedFile.listFiles();
						for (File temp : selectedFilelist) {
							millisec = temp.lastModified();
							Date dt = new Date(millisec);
							SimpleDateFormat sd = new SimpleDateFormat("YYYY-MM-dd");

							System.out.print(sd.format(dt) + "\t");

							if (temp.isDirectory()) {
								System.out.print("<DIR>");
							}
							System.out.println("\t" + temp.getName());
						}
						return;
					}
				}
			}
			System.out.println("�������� �ʴ� ���͸��Դϴ�");
		}
	}
}
