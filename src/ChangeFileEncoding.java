import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import org.apache.commons.io.FileUtils;

public class ChangeFileEncoding extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	/**
	 * @读取目录下文件，并转换为指定编码
	 */
	private JButton jbSrcDir;
	private JButton jbDesDir;
	private JTextField jtSrcDir;
	private JTextField jtDesDir;
	private JLabel jl_suffix;
	private JComboBox<String> jcbSuffix;
	private JLabel jlFrom;
	private JLabel jlTo;
	private JComboBox<String> jcbFromCoding;
	private JComboBox<String> jcbToCoding;
	private JButton jbStartChge;

	private String suffix = null;
	private String fromCoding = null;
	private String toCoding = null;
	private String srcDirPath = null;
	private String desDirPath = null;
	private JRadioButton rdIncludeSubDir; // 是否包含子目录
	
	private String[] allSuffixStr = {"c", "java", "cpp", "txt", "html", "php", "py", "rtf", "css", "jsp","其他"};
	private String EnCodes[] = {"GB2312", "UTF-8", "GBK", "UTF-16", "GB18030","cp936", "BIG5", "Latin1", "其他"};
	
	public static void main(String[] args) {

		ChangeFileEncoding cfc = new ChangeFileEncoding();
	}

	ChangeFileEncoding() {
		this.setTitle("编码转换器");
		
		GridBagLayout gridbag = new GridBagLayout(); // 布局
        GridBagConstraints cst = new GridBagConstraints();
        cst.fill = GridBagConstraints.BOTH;
//      cst.fill = GridBagConstraints.HORIZONTAL;
        cst.insets = new Insets (4,4,4,4);
        setLayout(gridbag);
       
        Container con = getContentPane();

		jbStartChge = new JButton("OK");
		jl_suffix = new JLabel("文件后缀：");
		jcbSuffix = new JComboBox<String>(allSuffixStr);
		jcbSuffix.setBounds(0, 0, 30, 40);
		jlFrom = new JLabel("编码方式从:");
		jcbFromCoding = new JComboBox<String>(EnCodes);
		jcbToCoding = new JComboBox<String>(EnCodes);
		jlTo = new JLabel("编码转换成:");
	
		jbSrcDir = new JButton("目录/文件");
		jbDesDir = new JButton("保存到");
		jtSrcDir = new JTextField();
		jtDesDir = new JTextField();
		
		rdIncludeSubDir = new JRadioButton("包含子目录");
	
		// 第一行 
		cst.weightx = 0;
		gridbag.setConstraints(jl_suffix, cst);
		con.add(jl_suffix);
		cst.weightx = 1;
		cst.gridwidth = GridBagConstraints.REMAINDER;
//		cst.fill = GridBagConstraints.BOTH;
		gridbag.setConstraints(jcbSuffix, cst);
		con.add(jcbSuffix);
		
		// 第二行 
		cst.weightx = 0;
		cst.gridwidth = GridBagConstraints.RELATIVE;
		gridbag.setConstraints(jlFrom, cst);
		con.add(jlFrom);
		
		cst.weightx = 1.8;
		cst.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(jcbFromCoding, cst);
		con.add(jcbFromCoding);
		
		// 第三行
		cst.weightx = 0;
		cst.gridwidth = GridBagConstraints.RELATIVE;
		gridbag.setConstraints(jlTo, cst);
		con.add(jlTo);
		
		cst.weightx = 1.8;
		cst.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(jcbToCoding, cst);
		con.add(jcbToCoding);
		
		// 第四行
		cst.weightx = 0;
		cst.gridwidth = GridBagConstraints.RELATIVE;
		gridbag.setConstraints(jbSrcDir, cst);
		con.add(jbSrcDir);   
		
		cst.weightx = 1.8;
		cst.gridwidth = GridBagConstraints.REMAINDER;
//		jtSrcDir.setMaximumSize(100);
//		cst.weighty = 0;
		gridbag.setConstraints(jtSrcDir, cst);
		con.add(jtSrcDir);
		
		// 第五行
		cst.weightx = 0;
		cst.gridwidth = GridBagConstraints.RELATIVE;
		gridbag.setConstraints(jbDesDir, cst);
		con.add(jbDesDir);
		cst.weightx = 1.8;
		cst.gridwidth = GridBagConstraints.REMAINDER;
		
		gridbag.setConstraints(jtDesDir, cst);
		con.add(jtDesDir);
		// 最后一行
		cst.weightx = 0;
		cst.gridwidth = GridBagConstraints.RELATIVE;
		gridbag.setConstraints(rdIncludeSubDir, cst);
		con.add(rdIncludeSubDir);
		cst.weightx = 1;
		cst.gridwidth = GridBagConstraints.REMAINDER;

		gridbag.setConstraints(jbStartChge, cst);
		con.add(jbStartChge);
		
		jcbFromCoding.addActionListener(this);
		jcbToCoding.addActionListener(this);
		
		jcbSuffix.addActionListener(this);
		jbSrcDir.addActionListener(this);
		jbDesDir.addActionListener(this);
		jbStartChge.addActionListener(this);
		rdIncludeSubDir.addActionListener(this);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation(400, 400);
		this.setSize(360, 240);
		this.setResizable(false);
		this.setVisible(true);
	}

	public static void println(Object obj) {
//		System.out.println(obj);
	}
	public void modifyFilesEncoding() {

		// 获取目录下所有文件（递归）转编码(源目录和目的目录可以相同)后缀java前没有点

		int nFiles = 0;

		if (srcDirPath != null) {
			nFiles = changeFileEncode(srcDirPath, desDirPath, fromCoding, toCoding, suffix,
				this.rdIncludeSubDir.isSelected());
		}
		if (-1 == nFiles) {
			JOptionPane.showMessageDialog(this,
					srcDirPath + "：目录不存在", "系统信息", JOptionPane.WARNING_MESSAGE);
		} 
		else {
			JOptionPane.showMessageDialog(this,
			       "转换了 " + nFiles + "个文件", "系统信息", JOptionPane.OK_OPTION);
		}
		
println("...change end...");
	}

	public int changeFileEncode(String srcDirPath, String destDirPath,
			String encodeFrom, String encodeTo, String suffix, boolean isIncludeChild) 
	{
		if (encodeFrom == null || null == encodeTo) {
			return 0;
		}
		
		int count = 0;
		File file = new File(srcDirPath);
		File desFile = new File(destDirPath);
		
println("srcDir_path:" + srcDirPath);
println("destDir_path:" + destDirPath);
		if (null == file ||  !file.exists() || null == desFile || !desFile.isDirectory()) {
//println("error.");
			return -1;
		}
		
		String desFilePath = null;
		String srcCharacter = null;
		Collection<File> srcFileCollec  = null;
		
		if (true == file.isFile()) {  // 单个文件
			srcCharacter = CharacterEnding.getFileCharacterEnding(file);
			desFilePath = desFile.getAbsolutePath() + File.separator + file.getName(); 
			// 使用codeFrom读取数据，然后用codeTo写入数据
			try {
				File out = new File(desFilePath);
				// 优先使用检测到源文件的编码
				if (srcCharacter != null && !srcCharacter.equalsIgnoreCase(encodeFrom)) {
					FileUtils.writeLines(out, encodeTo, FileUtils.readLines(file, srcCharacter));
				}
				else {
					FileUtils.writeLines(out, encodeTo, FileUtils.readLines(file, encodeFrom));					
				}
				count++;
			} catch (IOException e) {

				e.printStackTrace();
			}
			
			return count;
		} 
		else { // 是目录
			
		// 这个jar包 http://commons.apache.org/io/download_io.cgi 进行编码转换
			srcFileCollec = FileUtils.listFiles(file, new String[] { suffix }, isIncludeChild);
		}
		
		String srcAbsolute = null;

		for (File srcFile : srcFileCollec) {
			// 检测源文件编码格式
			srcCharacter = CharacterEnding.getFileCharacterEnding(srcFile);
println(srcFile + "  " + srcCharacter);
			// 文件觉对路径
			srcAbsolute = srcFile.getAbsolutePath();
			// 拼接src目录名底下的子项
			desFilePath = destDirPath +srcAbsolute.substring(srcDirPath.length()); 
			// 使用codeFrom读取数据，然后用codeTo写入数据
			try {
				File out = new File(desFilePath);
				// 优先使用检测到源文件的编码
				if (srcCharacter != null && !srcCharacter.equalsIgnoreCase(encodeFrom)) {
					FileUtils.writeLines(out, encodeTo, FileUtils.readLines(srcFile, srcCharacter));
				}
				else {
					FileUtils.writeLines(out, encodeTo, FileUtils.readLines(srcFile, encodeFrom));					
				}
			} catch (IOException e) {

				e.printStackTrace();
			}
			count++;
			srcCharacter = null;
println("to:" + desFilePath + "   :" + count);
		}
		
		return count;
	}
	
	public  File getFile() {

		final JFileChooser fc = new JFileChooser();

		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		// JFileChooser.FILES_ONLY FILES_AND_DIRECTORIES
		// JFileChooser.DIRECTORIES_ONLY
		
		int returnVal = fc.showOpenDialog(this);
		
		File file_choosed = null;
		if (JFileChooser.CANCEL_OPTION != returnVal)
			file_choosed = fc.getSelectedFile();

		return file_choosed;

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == jbStartChge) {

			suffix = ( (String)jcbSuffix.getSelectedItem() ).trim();
			fromCoding = ((String) this.jcbFromCoding.getSelectedItem()).trim();
			toCoding = ((String) this.jcbToCoding.getSelectedItem()).trim();
			srcDirPath = jtSrcDir.getText();
			desDirPath = jtDesDir.getText();
			
			if(null == srcDirPath || null == desDirPath || null == suffix || null == fromCoding 
					|| null == toCoding || srcDirPath.length() < 1 || desDirPath.length() < 1
					|| suffix.length() < 1 || fromCoding.length() < 1 || toCoding.length() < 1 )
			{
				JOptionPane.showMessageDialog(this,
					       "输入框不能为空，请检查!", "系统信息", JOptionPane.ERROR_MESSAGE);
				
				return;
			}
//println("Encodes:" + fromCoding + " -> " + toCoding);			
			modifyFilesEncoding();
			
		} else if (e.getSource() == jbSrcDir) {
			
			File file = getFile();
			if (null == file) {
//println("select nothing !");
				return;
			}
			
			srcDirPath = file.getAbsolutePath();
			jtSrcDir.setText(srcDirPath);
			
			if (file.isFile()) {
				desDirPath = file.getParent();
			} else { //目录
				desDirPath = srcDirPath;
			}
			jtDesDir.setText(desDirPath);
println("DesDirPath " + desDirPath);
			
		} else if (e.getSource() == jbDesDir) {
			
			File dir = getFile();
			if (null == dir || dir.isFile()) { //存放目的地必须是目录
				
				return;
			}
			desDirPath = dir.getAbsolutePath();
			jtDesDir.setText(desDirPath);
		} else if (e.getSource() == jcbSuffix) {
			if (jcbSuffix.getSelectedItem().equals("其他")) {
				jcbSuffix.setEditable(true);      // 设置选择框为可编辑
				jcbSuffix.setSelectedItem("");  //清空
			}
			else {
				jcbSuffix.setEditable(false);   
			}
		} else if (e.getSource() == jcbFromCoding) {
			if (jcbFromCoding.getSelectedItem().equals("其他")) {
				jcbFromCoding.setEditable(true);
				jcbFromCoding.setSelectedItem("");  //清空
			}
			else {
				jcbFromCoding.setEditable(false);
			}
			
		}else if (e.getSource() == jcbToCoding) {
			if (jcbToCoding.getSelectedItem().equals("其他")) {
				jcbToCoding.setEditable(true);
				jcbToCoding.setSelectedItem("");  //清空
			}else {
				jcbToCoding.setEditable(false);
			}
		}
		
	}	

}
