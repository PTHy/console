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
	static int delchk = 0;

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
				} else if (command.equalsIgnoreCase("ren")) {
					ren();
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
			System.out.println("하위 디렉터리 또는 파일 " + mkdir.getName() + "이(가) 이미 있습니다.");
		}
		if (!mkdir.mkdirs()) {
			System.out.println("명령 구문이 올바르지 않습니다.");
			return;
		}
	}

	// exit - exit

	public static void exit() {
		System.exit(0);
	}

	// del - Delete File /p /s /f

	public static void delContent_1(File file) throws IOException {
		File[] list = file.listFiles();
		Scanner sc = new Scanner(System.in);
		String chk;

		while (true) {
			System.out.print(file.getCanonicalPath() + "\\* 계속하시겠습니까(Y/N)?");
			chk = sc.nextLine();
			if (chk.equalsIgnoreCase("y"))
				break;
			else if (chk.equalsIgnoreCase("n"))
				return;
			else
				continue;
		}
		for (File tmp : list) {
			if (tmp.isDirectory()) {
				delContent_1(tmp);
			}
			if (tmp.isFile()) {
				if (tmp.canWrite() && !tmp.isHidden()) {
					if (tmp.delete()) {
						System.out.println("파일 삭제 - " + tmp.getCanonicalPath());
						delchk++;
					}
				} else if (!tmp.canWrite()) {
					System.out.println(tmp.getCanonicalPath());
					System.out.println("엑세스가 거부되었습니다.");
				}
			}
		}
	}

	public static void delContent_2(File file, String delFile) throws IOException {
		File[] list = file.listFiles();
		for (File tmp : list) {
			if (tmp.isDirectory()) {
				delContent_2(tmp, delFile);
			}
			if (tmp.getName().equalsIgnoreCase(delFile)) {
				if (tmp.canWrite() && !tmp.isHidden()) {
					if (tmp.delete()) {
						System.out.println("파일 삭제 - " + tmp.getCanonicalPath());
						delchk++;
					}
				} else if (!tmp.canWrite()) {
					System.out.println(tmp.getCanonicalPath());
					System.out.println("엑세스가 거부되었습니다.");
				}
			}
		}
	}

	public static void del() throws IOException {
		String inputPath = argArr[1];
		Scanner sc = new Scanner(System.in);
		String chk;
		if (!(argArr[1].indexOf(":") > -1)) {
			inputPath = curDir.getCanonicalPath() + "\\" + argArr[1];
		}
		if (argArr.length == 3) {
			inputPath = argArr[1];
			if (!(argArr[1].indexOf(":") > -1)) {
				inputPath = curDir.getCanonicalPath() + "\\" + argArr[2];
			}
		}
		File mkdir = new File(inputPath);
		File[] list;

		if (argArr.length == 3 && argArr[1].equalsIgnoreCase("/p")) {
			if (!mkdir.exists()) {
				System.out.println(mkdir.getCanonicalPath() + "을(를) 찾을 수 없습니다.");
				return;
			}
			if (mkdir.isDirectory()) {
				list = mkdir.listFiles();
				for (File file : list) {
					if(file.isFile()) {
						while(true) {
							if (file.isFile()) {
								System.out.print(file.getCanonicalPath() + ", 삭제하시겠습니까(Y/N)?");
								chk = sc.nextLine();
								if (chk.equalsIgnoreCase("Y")) {
									file.delete();
									break;
								} else if (chk.equalsIgnoreCase("N")) {
									break;
								} else {
									continue;
								}
							}
						}
					}
				}
				System.out.println("");
			}
			if(mkdir.isFile() && mkdir.isHidden()) {
				System.out.println("지정한 파일을 찾을 수 없습니다.");
				return;
			}
			if (!mkdir.isDirectory()) {
				while (true) {
					System.out.print(mkdir.getCanonicalPath() + ", 삭제하시겠습니까(Y/N)?");
					chk = sc.nextLine();
					if (chk.equalsIgnoreCase("Y"))
						break;
					else if (chk.equalsIgnoreCase("N"))
						return;
					else
						continue;
				}
				mkdir.delete();
				System.out.println();
			}
		} else if (argArr.length == 3 && argArr[1].equalsIgnoreCase("/s")) {
			if (mkdir.isDirectory()) {
				delContent_1(mkdir);
				delchk++;
			} else if (mkdir.isFile()) {
				delContent_2(curDir, mkdir.getName());
			}
			if (delchk == 0)
				System.out.println(mkdir.getCanonicalPath() + "을(를) 찾을 수 없습니다.");
			delchk = 0;
		} else if (argArr.length == 3 && argArr[1].equalsIgnoreCase("/f")) {
			if (!mkdir.exists()) {
				System.out.println(mkdir.getCanonicalPath() + "을(를) 찾을 수 없습니다.");
				return;
			}
			if (mkdir.isDirectory()) {
				list = mkdir.listFiles();
				while (true) {
					System.out.print(mkdir.getCanonicalPath() + "\\*, 계속하시겠습니까(Y/N)?");
					chk = sc.nextLine();
					if (chk.equalsIgnoreCase("Y")) {
						for (File file : list) {
							if (file.isFile()) {
								file.delete();
							}
						}
						return;
					} else if (chk.equalsIgnoreCase("N")) {
						return;
					}
				}
			}
			if (!mkdir.isDirectory()) {
				mkdir.delete();
				System.out.println("");
			}
		} else {
			if (!mkdir.exists()) {
				System.out.println(mkdir.getCanonicalPath() + "을(를) 찾을 수 없습니다.");
				return;
			}
			if (mkdir.isDirectory()) {
				list = mkdir.listFiles();
				while (true) {
					System.out.print(mkdir.getCanonicalPath() + "\\*, 계속하시겠습니까(Y/N)?");
					chk = sc.nextLine();
					if (chk.equalsIgnoreCase("Y")) {
						for (File file : list) {
							if (file.isFile()) {
								if (file.canWrite()) {
									file.delete();
								} else if (!file.canWrite()) {
									System.out.println(file.getCanonicalPath());
									System.out.println("엑세스가 거부되었습니다.");
								}
							}
						}
						return;
					} else if (chk.equalsIgnoreCase("N")) {
						return;
					}
				}
			}
			if (!mkdir.isDirectory()) {
				if (mkdir.canWrite()) {
					mkdir.delete();
				} else if (!mkdir.canWrite()) {
					System.out.println(mkdir.getCanonicalPath());
					System.out.println("엑세스가 거부되었습니다.");
				}
			}
		}

	}

	// ren - Rename File

	public static void ren() throws IOException {
		File originFile;
		File newFile;
		String originPath;
		String newPath;

		originPath = curDir.getCanonicalPath() + "\\" + argArr[1];
		newPath = curDir.getCanonicalPath() + "\\" + argArr[2];
		if (argArr.length == 3) {
			originFile = new File(originPath);
			newFile = new File(newPath);

			if (originFile.exists()) {
				if (!newFile.exists()) {
					originFile.renameTo(newFile);
				} else {
					System.out.println("중복되는 파일 이름이 있거나 파일을 찾을 수 없습니다.");
				}
			} else {
				System.out.println("originPath : " + originPath + " newPath : " + newPath);
				System.out.println("지정된 파일을 찾을 수 없습니다");
				return;
			}
		} else {
			System.out.println("명령 구문이 올바르지 않습니다.");
			return;
		}
	}

	// deltree - Delete All Directory

	public static void deltree() throws IOException {
		String inputPath = argArr[1];
		Scanner sc = new Scanner(System.in);
		String chk;
		if (!(argArr[1].indexOf(":") > -1)) {
			inputPath = curDir.getCanonicalPath() + "\\" + argArr[1];
		}
		File mkdir = new File(inputPath);
		if (!mkdir.exists()) {
			System.out.println("지정한 파일을 찾을 수 없습니다.");
			return;
		}
		rdContent(mkdir);
		mkdir.delete();
	}

	// rd - Delete Directory /s /q

	public static void rdContent(File file) {
		File[] list = file.listFiles();
		for (File tmp : list) {
			if (tmp.isDirectory()) {
				rdContent(tmp);
			}
			tmp.delete();
		}
	}

	public static void rd() throws IOException {
		String inputPath = argArr[1];
		Scanner sc = new Scanner(System.in);
		String chk;
		int qCnt = 0;
		if ((argArr.length == 3 || argArr.length == 4) && argArr[1].equalsIgnoreCase("/s")) {
			inputPath = argArr[2];
			if (!(argArr[2].indexOf(":") > -1)) {
				inputPath = curDir.getCanonicalPath() + "\\" + argArr[2];
			}
			if (argArr.length == 4 && argArr[2].equalsIgnoreCase("/q")) {
				inputPath = argArr[3];
				if (!(argArr[3].indexOf(":") > -1)) {
					inputPath = curDir.getCanonicalPath() + "\\" + argArr[3];
				}
				qCnt = 1;
			}

			File mkdir = new File(inputPath);

			if (!mkdir.exists()) {
				System.out.println("지정한 파일을 찾을 수 없습니다.");
				return;
			}

			if (qCnt == 0) {
				while (true) {
					System.out.print(mkdir.getName() + ", 계속하시겠습니까(Y/N)?");
					chk = sc.nextLine();
					if (chk.equalsIgnoreCase("y"))
						break;
					else if (chk.equalsIgnoreCase("n"))
						return;
					else
						continue;
				}
			}
			rdContent(mkdir);
			mkdir.delete();
		} else if (argArr.length == 2) {
			if (!(argArr[1].indexOf(":") > -1)) {
				inputPath = curDir.getCanonicalPath() + "\\" + argArr[1];
			}
			File mkdir = new File(inputPath);
			if (!mkdir.exists()) {
				System.out.println("지정한 파일을 찾을 수 없습니다.");
				return;
			}

			File[] exitChk;
			exitChk = mkdir.listFiles();
			if (exitChk[0] == null) {
				mkdir.delete();
				return;
			} else {
				System.out.println("디렉터리가 비어 있지 않습니다.");
				return;

			}
		}
	}

	// Copy - Copy File /y /-y

	public static void copy() throws IOException {

		File[] list = curDir.listFiles();
		int originCnt = 0, copyCnt = 0, i, cnt = 0;
		;
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
				System.out.println("지정된 파일을 찾을 수 없습니다.");
			} else if (copyCnt == 1) {
				originFile = new File(originPath);
				copyFile = new File(copyPath);
				FileInputStream fis = new FileInputStream(originFile);
				FileOutputStream fos = new FileOutputStream(copyPath);

				try {
					while ((i = fis.read()) != -1) {

						// FileOutputStream에 읽은 데이터를 출력한다.
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
				cnt++;
				System.out.println("\t" + cnt + "개 파일이 복사되었습니다.");
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

						// FileOutputStream에 읽은 데이터를 출력한다.
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
				cnt++;
				System.out.println("\t" + cnt + "개 파일이 복사되었습니다.");
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
				System.out.println("지정된 파일을 찾을 수 없습니다.");
			} else if (copyCnt == 1) {
				Scanner sc = new Scanner(System.in);
				String input;
				originFile = new File(originPath);
				copyFile = new File(copyPath);
				FileInputStream fis = new FileInputStream(originFile);
				FileOutputStream fos = new FileOutputStream(copyPath);

				while (true) {
					System.out.print(copyFile.getName() + "을(를) 덮어쓰시겠습니까? (Yes/No): ");
					input = sc.nextLine();

					if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes"))
						break;
					else if (input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no"))
						return;
					else
						continue;
				}

				try {
					while ((i = fis.read()) != -1) {
						// FileOutputStream에 읽은 데이터를 출력한다.
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
				cnt++;
				System.out.println("\t" + cnt + "개 파일이 복사되었습니다.");
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

						// FileOutputStream에 읽은 데이터를 출력한다.
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
				cnt++;
				System.out.println("\t" + cnt + "개 파일이 복사되었습니다.");
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
				System.out.println("originPath : " + originPath + " copyPath : " + copyPath);
				System.out.println("지정된 파일을 찾을 수 없습니다.");
			} else if (copyCnt == 1) {
				Scanner sc = new Scanner(System.in);
				String input;
				originFile = new File(originPath);
				copyFile = new File(copyPath);
				FileInputStream fis = new FileInputStream(originFile);
				FileOutputStream fos = new FileOutputStream(copyPath);

				while (true) {
					System.out.print(copyFile.getName() + "을(를) 덮어쓰시겠습니까? (Yes/No): ");
					input = sc.nextLine();

					if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes"))
						break;
					else if (input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no"))
						return;
					else
						continue;
				}

				try {
					while ((i = fis.read()) != -1) {
						// FileOutputStream에 읽은 데이터를 출력한다.
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
				cnt++;
				System.out.println("\t" + cnt + "개 파일이 복사되었습니다.");
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

						// FileOutputStream에 읽은 데이터를 출력한다.
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
				cnt++;
				System.out.println("\t" + cnt + "개 파일이 복사되었습니다.");
			}
		} else {
			System.out.println("잘못된 접근입니다");
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
				System.out.println("지정한 파일이 존재하지 않습니다");
				return;
			}
			System.out.println();
		} else {
			System.out.println("명령 구문이 올바르지 않습니다.");
		}
	}

	// cd - Move Directory

	public static void cd() {
		if (argArr.length == 1 && !argArr[0].equalsIgnoreCase("cd..")) {
			System.out.println(curDir);
			return;
		} else if (argArr.length > 2) {
			System.out.println("잘못된 접근입니다");
			return;
		} else if (argArr[0].equalsIgnoreCase("cd..") || argArr[1].equals("..")) {
			File newDir = curDir.getParentFile();

			if (newDir == null) {
				System.out.println("유효하지 않은 접근입니다");
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
				System.out.println("유효하지 않은 접근입니다");
				return;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Dir - File list /l /d

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
		} else if (argArr.length == 2 && argArr[1].equalsIgnoreCase("/ad")) {
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
		} else if (argArr.length == 2 && argArr[1].equalsIgnoreCase("/b")) {
			for (File tempFile : list) {
				System.out.println(tempFile.getName());
			}
		} else if (argArr.length == 2 && argArr[1].equalsIgnoreCase("/w")) {
			for (File tempFile : list) {
				System.out.print("[" + tempFile.getName() + "]\t");
			}
			System.out.println();
		} else if (argArr.length == 2 && argArr[1].equalsIgnoreCase("/d")) {
			for (File tempFile : list) {
				System.out.print("[" + tempFile.getName() + "]\n");
			}
			System.out.println();
		} else if (argArr.length == 2 && argArr[1].equalsIgnoreCase("/l")) {
			for (File tempFile : list) {
				millisec = tempFile.lastModified();
				Date dt = new Date(millisec);
				SimpleDateFormat sd = new SimpleDateFormat("YYYY-MM-dd");

				System.out.print(sd.format(dt) + "\t");

				if (tempFile.isDirectory()) {
					System.out.print("<DIR>");
				}
				System.out.println("\t" + tempFile.getName().toLowerCase());
			}
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
			System.out.println("존재하지 않는 디렉터리입니다");
		}
	}

}
