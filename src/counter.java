import java.awt.*;
import java.awt.RenderingHints.Key;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import javax.swing.*;

/**
*2016年11月
*作者：whthend
*与win自带计算器功能界面相仿。支持历史记录功能。不支持键盘输入。
*/

public class counter extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
    public static void main(String args[]) {
        counter calculator = new counter();
        calculator.setVisible(true);
        calculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    /*
     * 构建主题窗口及按键
     */
	private final String[] key = { "%","sqrt","log","1/x",
							"CE" ,"C", "Back","/",
							"7", "8", "9", "*",
							"4", "5", "6","-",
							"1", "2", "3", "+", 
							"+/-", "0", ".", "=" };
	private JButton Button_key[] = new JButton[key.length];
    private Label lab; 
	/*
	 * 构建文本系统
	 */
    private File file;//定义文件
	private JTextField resultText = new JTextField("0");
	private JTextArea histroy = new JTextArea();
	static String s=null;    //用于传递字符
	
	public counter(){

        this.setTitle("计算器");
        this.setSize(300,400);
        this.setLocation(800, 350);
        JMenuBar jmb = createMenuBar();   //创建菜单栏
        this.setJMenuBar(jmb);  
        this.windows();					//创建文本框、文本区域及按键

    }
	private JMenuBar createMenuBar() {  
        // 实例化一个JMenuBar的对象  
        JMenuBar jmb = new JMenuBar(); 
        JMenu menu = new JMenu("文件");   
        jmb.add(menu);
        JMenu help = new JMenu("帮助");   
        jmb.add(help); 
        JMenuItem save = new JMenuItem("保存历史");
        menu.add(save);
        JMenuItem clean = new JMenuItem("清空历史");
        menu.add(clean);
        JMenuItem exit = new JMenuItem("退出");
        menu.add(exit);
        JMenuItem about = new JMenuItem("关于");
        help.add(about);
        exit.addActionListener(this);
        about.addActionListener(this);
        save.addActionListener(this);
        clean.addActionListener(this);
        return jmb;  
    }  

  /**
   * 创建窗口
   * 采用BorederLayout+GridLayout布局方式
   */
    public void windows() {	
        lab = new Label();
        this.add(lab);
        // 文本框中的内容采用右对齐方式\背景颜色为白色
        resultText.setHorizontalAlignment(JTextField.RIGHT);
        resultText.setEditable(false);
        resultText.setBackground(Color.WHITE);
        resultText.setFont(new Font("楷体", Font.BOLD, 20));
        JPanel top = new JPanel();
        top.setLayout(new BorderLayout());
        top.add("Center", resultText);
        
        histroy.setFont(new Font("楷体", Font.BOLD, 25));
        
        histroy.setEditable(false);						//不知道为啥木有生效！！！！！
        
        JPanel his = new JPanel();						//创建文本区域，显示记录
        his.setLayout(new BorderLayout());
        his.add("Center", histroy);
        JScrollPane scrollPane=new JScrollPane(histroy=new JTextArea());    //添加滑动条
        his.add(scrollPane,BorderLayout.CENTER);

        // 使用网格布局器，创建计算器上键的按钮，将键放在一个画板内
        JPanel Button1 = new JPanel();
        Button1.setLayout(new GridLayout(6, 4, 0, 0));
        for (int i = 0; i < key.length; i++) {
        	Button_key[i] = new JButton(key[i]);
        	Button1.add(Button_key[i]);
            Button_key[i].setForeground(Color.black);
        }      
        // 为各按钮添加事件侦听器
        for (int i = 0; i < key.length; i++) {
        	Button_key[i].addActionListener(this);
        }       
        // 整体布局
        getContentPane().setLayout(new BorderLayout(3, 1));
        getContentPane().add("Center", his);
        getContentPane().add("North", top);
        getContentPane().add("South", Button1);
    }
    
	/**
	 * 运算及按键控制
	 */
    private boolean firstDigit = true;
    private double resultNum = 0.0;
    private String operator = "=";
    private boolean operateValidFlag = true;
  
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自动生成的方法存根
        String ack = e.getActionCommand();
        if (ack.equals(key[6])) {
            Back();
        }else if (ack.equals(key[5])) {
        	 resultText.setText("0");
        	 firstDigit = true;
        	 operator = "=";
        }else if (ack.equals(key[4])) {
        	resultText.setText("");
        }  else if ("0123456789.".indexOf(ack) >= 0) {
            Number(ack);
        } else {
            Operator(ack);
        }
        
        if (e.getActionCommand().equals("保存历史")){
			   save();
		 }
        else if(e.getActionCommand().equals("清空历史"))
		 {
			 histroy.setText("");
		}
        else if(e.getActionCommand().equals("退出"))
		{
			 System.exit(0);
		}
        else if(e.getActionCommand().equals("关于"))
		{
			 about();   
		}
        else if (e.getActionCommand().equals("关于"))
		{
			 about();   
		}
	}
	
    /**
     * 处理Backspace键被按下的事件
     */
    private void Back() {
        String text = resultText.getText();
        int i = text.length();
        if (i > 0) {
            text = text.substring(0, i - 1);
            if (text.length() == 0) {
                resultText.setText("0");
                firstDigit = true;
                operator = "=";
            } else {
                resultText.setText(text);
            }
        }
    }

 
    /*
     * 处理数字键被按下的事件
     */
    private void Number(String key) {
        if (firstDigit) {
            resultText.setText(key);
        } else if ((key.equals(".")) && (resultText.getText().indexOf(".") < 0)) {
            resultText.setText(resultText.getText() + ".");
        } else if (!key.equals(".")) {
            resultText.setText(resultText.getText() + key);
        }
        firstDigit = false;
    }
 
    /*
     * 处理运算符键被按下的事件
     */
    private void Operator(String key) {
    	Double i,b;
    	
    	if (operator.equals("/")) {
            // 除法运算
            // 如果当前结果文本框中的值等于0
            if (getNumber() == 0.0) {
                // 操作不合法
                operateValidFlag = false;
                resultText.setText("除数不能为零");
            } else {
            	i=resultNum;
            	b=getNumber();
                resultNum /= getNumber();
                s=String.valueOf(i)+"/"+String.valueOf(b)+"="+String.valueOf(resultNum)+"\r\n";
                diplay(s);
            }
        } else if (operator.equals("1/x")) {
            // 倒数运算
            if (resultNum == 0.0) {
                // 操作不合法
                operateValidFlag = false;
                resultText.setText("零没有倒数");
            } else {
            	i=resultNum;
                resultNum = 1 / resultNum;
                s="1/"+String.valueOf(i)+"="+String.valueOf(resultNum)+"\r\n";
                diplay(s);
            }
        } else if (operator.equals("+")) {
            // 加法运算
        	i=resultNum;
        	b=getNumber();
            resultNum += getNumber();
            s=String.valueOf(i)+"+"+String.valueOf(b)+"="+String.valueOf(resultNum)+"\r\n";
            diplay(s);
        } else if (operator.equals("-")) {
            // 减法运算
        	i=resultNum;
        	b=getNumber();
            resultNum -= getNumber();            
            s=String.valueOf(i)+"-"+String.valueOf(b)+"="+String.valueOf(resultNum)+"\r\n";
            diplay(s);
        } else if (operator.equals("*")) {
            // 乘法运算
        	i=resultNum;
        	b=getNumber();
            resultNum *= getNumber();
            s=String.valueOf(i)+" x "+String.valueOf(b)+"="+String.valueOf(resultNum)+"\r\n";
            diplay(s);
        } else if (operator.equals("sqrt")) {
            // 平方根运算
        	i=resultNum;
            resultNum = Math.sqrt(resultNum);
            s=String.valueOf(i)+"的平方根="+String.valueOf(resultNum)+"\r\n";
            diplay(s);
        }else if (operator.equals("%")) {
            // 百分号运算，除以100
        	i=resultNum;
            resultNum = resultNum / 100;
            s=String.valueOf(i)+"="+String.valueOf(resultNum)+"%\r\n";
            diplay(s);
        } else if (operator.equals("+/-")) {
            // 正数负数运算
        	i=resultNum;
            resultNum = resultNum * (-1);
        } else if (operator.equals("sin")) {
            // 正数负数运算
        	i=resultNum;
            resultNum =Math.sin(resultNum/180*Math.PI);
            s="sin("+String.valueOf(i)+")="+String.valueOf(resultNum)+"\r\n";
            diplay(s);
        } else if (operator.equals("=")) {
            // 赋值运算
            resultNum = getNumber();
        }else if (operator.equals("log")) {
            // 对数运算
        	if (resultNum<=0.0) {
                // 操作不合法
                operateValidFlag = false;
                resultText.setText("请输入大于0的数");
            } else {
        	i=resultNum;
            resultNum = Math.log(resultNum);
            s="log("+String.valueOf(i)+")="+String.valueOf(resultNum)+"\r\n";
            diplay(s);
            }
        }
        if (operateValidFlag) {
            long t1;
            double t2;
            t1 = (long) resultNum;
            t2 = resultNum - t1;
            if (t2 == 0) {
                resultText.setText(String.valueOf(t1));
            } else {
                resultText.setText(String.valueOf(resultNum));
            }
        }
        // 运算符等于用户按的按钮
        operator = key;
        firstDigit = true;
        operateValidFlag = true;
    }
 
    /*
     * 从结果文本框中获取数字
     */
    private double getNumber() {
        double result = 0;
        try {
            result = Double.valueOf(resultText.getText()).doubleValue();
        } catch (NumberFormatException e) {
        }
        return result;
    }
    /*
     * 向历史记录文本区域中添加条目
     */
    private void save(){
    	 if (file == null) {
    		 FileDialog save = new FileDialog(this, "保存", FileDialog.SAVE);
             save.setVisible(true);//显示保存文件对话框
             String dirpath = save.getDirectory();//获取保存文件路径并保存到字符串中。
             String fileName = save.getFile();////获取打保存文件名称并保存到字符串中
             
             if (dirpath == null || fileName == null)//判断路径和文件是否为空
                 return;//空操作
             else
                 file=new File(dirpath,fileName);//文件不为空，新建一个路径和名称
         }
             try {
                 BufferedWriter bufw = new BufferedWriter(new FileWriter(file));
                 
                 String text = histroy.getText();//获取文本内容
                 bufw.write(text);//将获取文本内容写入到字符输出流
                 bufw.close();//关闭文件
             } catch (IOException e1) {
                 //抛出IO异常
                 e1.printStackTrace();
             }
    }
 
    private void diplay(String s){

        String a = null;    
        Calendar time = Calendar.getInstance();  
        a =time.get(Calendar.HOUR_OF_DAY)+"时"+time.get(Calendar.MINUTE)+"分"+time.get(Calendar.SECOND)+"秒  :   "; 
        histroy.append(a);
    	histroy.append(s);
    }

    private void about(){
    	
        JOptionPane.showMessageDialog(null, "作者：王涵\r\n班级：计算机1401\r\n学号：20145293\r\n时间：2016年11月", "关于",JOptionPane.INFORMATION_MESSAGE);
    	
    }

}
