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
*2016��11��
*���ߣ�whthend
*��win�Դ����������ܽ�����¡�֧����ʷ��¼���ܡ���֧�ּ������롣
*/

public class counter extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
    public static void main(String args[]) {
        counter calculator = new counter();
        calculator.setVisible(true);
        calculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    /*
     * �������ⴰ�ڼ�����
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
	 * �����ı�ϵͳ
	 */
    private File file;//�����ļ�
	private JTextField resultText = new JTextField("0");
	private JTextArea histroy = new JTextArea();
	static String s=null;    //���ڴ����ַ�
	
	public counter(){

        this.setTitle("������");
        this.setSize(300,400);
        this.setLocation(800, 350);
        JMenuBar jmb = createMenuBar();   //�����˵���
        this.setJMenuBar(jmb);  
        this.windows();					//�����ı����ı����򼰰���

    }
	private JMenuBar createMenuBar() {  
        // ʵ����һ��JMenuBar�Ķ���  
        JMenuBar jmb = new JMenuBar(); 
        JMenu menu = new JMenu("�ļ�");   
        jmb.add(menu);
        JMenu help = new JMenu("����");   
        jmb.add(help); 
        JMenuItem save = new JMenuItem("������ʷ");
        menu.add(save);
        JMenuItem clean = new JMenuItem("�����ʷ");
        menu.add(clean);
        JMenuItem exit = new JMenuItem("�˳�");
        menu.add(exit);
        JMenuItem about = new JMenuItem("����");
        help.add(about);
        exit.addActionListener(this);
        about.addActionListener(this);
        save.addActionListener(this);
        clean.addActionListener(this);
        return jmb;  
    }  

  /**
   * ��������
   * ����BorederLayout+GridLayout���ַ�ʽ
   */
    public void windows() {	
        lab = new Label();
        this.add(lab);
        // �ı����е����ݲ����Ҷ��뷽ʽ\������ɫΪ��ɫ
        resultText.setHorizontalAlignment(JTextField.RIGHT);
        resultText.setEditable(false);
        resultText.setBackground(Color.WHITE);
        resultText.setFont(new Font("����", Font.BOLD, 20));
        JPanel top = new JPanel();
        top.setLayout(new BorderLayout());
        top.add("Center", resultText);
        
        histroy.setFont(new Font("����", Font.BOLD, 25));
        
        histroy.setEditable(false);						//��֪��Ϊɶľ����Ч����������
        
        JPanel his = new JPanel();						//�����ı�������ʾ��¼
        his.setLayout(new BorderLayout());
        his.add("Center", histroy);
        JScrollPane scrollPane=new JScrollPane(histroy=new JTextArea());    //��ӻ�����
        his.add(scrollPane,BorderLayout.CENTER);

        // ʹ�����񲼾����������������ϼ��İ�ť����������һ��������
        JPanel Button1 = new JPanel();
        Button1.setLayout(new GridLayout(6, 4, 0, 0));
        for (int i = 0; i < key.length; i++) {
        	Button_key[i] = new JButton(key[i]);
        	Button1.add(Button_key[i]);
            Button_key[i].setForeground(Color.black);
        }      
        // Ϊ����ť����¼�������
        for (int i = 0; i < key.length; i++) {
        	Button_key[i].addActionListener(this);
        }       
        // ���岼��
        getContentPane().setLayout(new BorderLayout(3, 1));
        getContentPane().add("Center", his);
        getContentPane().add("North", top);
        getContentPane().add("South", Button1);
    }
    
	/**
	 * ���㼰��������
	 */
    private boolean firstDigit = true;
    private double resultNum = 0.0;
    private String operator = "=";
    private boolean operateValidFlag = true;
  
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO �Զ����ɵķ������
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
        
        if (e.getActionCommand().equals("������ʷ")){
			   save();
		 }
        else if(e.getActionCommand().equals("�����ʷ"))
		 {
			 histroy.setText("");
		}
        else if(e.getActionCommand().equals("�˳�"))
		{
			 System.exit(0);
		}
        else if(e.getActionCommand().equals("����"))
		{
			 about();   
		}
        else if (e.getActionCommand().equals("����"))
		{
			 about();   
		}
	}
	
    /**
     * ����Backspace�������µ��¼�
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
     * �������ּ������µ��¼�
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
     * ����������������µ��¼�
     */
    private void Operator(String key) {
    	Double i,b;
    	
    	if (operator.equals("/")) {
            // ��������
            // �����ǰ����ı����е�ֵ����0
            if (getNumber() == 0.0) {
                // �������Ϸ�
                operateValidFlag = false;
                resultText.setText("��������Ϊ��");
            } else {
            	i=resultNum;
            	b=getNumber();
                resultNum /= getNumber();
                s=String.valueOf(i)+"/"+String.valueOf(b)+"="+String.valueOf(resultNum)+"\r\n";
                diplay(s);
            }
        } else if (operator.equals("1/x")) {
            // ��������
            if (resultNum == 0.0) {
                // �������Ϸ�
                operateValidFlag = false;
                resultText.setText("��û�е���");
            } else {
            	i=resultNum;
                resultNum = 1 / resultNum;
                s="1/"+String.valueOf(i)+"="+String.valueOf(resultNum)+"\r\n";
                diplay(s);
            }
        } else if (operator.equals("+")) {
            // �ӷ�����
        	i=resultNum;
        	b=getNumber();
            resultNum += getNumber();
            s=String.valueOf(i)+"+"+String.valueOf(b)+"="+String.valueOf(resultNum)+"\r\n";
            diplay(s);
        } else if (operator.equals("-")) {
            // ��������
        	i=resultNum;
        	b=getNumber();
            resultNum -= getNumber();            
            s=String.valueOf(i)+"-"+String.valueOf(b)+"="+String.valueOf(resultNum)+"\r\n";
            diplay(s);
        } else if (operator.equals("*")) {
            // �˷�����
        	i=resultNum;
        	b=getNumber();
            resultNum *= getNumber();
            s=String.valueOf(i)+" x "+String.valueOf(b)+"="+String.valueOf(resultNum)+"\r\n";
            diplay(s);
        } else if (operator.equals("sqrt")) {
            // ƽ��������
        	i=resultNum;
            resultNum = Math.sqrt(resultNum);
            s=String.valueOf(i)+"��ƽ����="+String.valueOf(resultNum)+"\r\n";
            diplay(s);
        }else if (operator.equals("%")) {
            // �ٷֺ����㣬����100
        	i=resultNum;
            resultNum = resultNum / 100;
            s=String.valueOf(i)+"="+String.valueOf(resultNum)+"%\r\n";
            diplay(s);
        } else if (operator.equals("+/-")) {
            // ������������
        	i=resultNum;
            resultNum = resultNum * (-1);
        } else if (operator.equals("sin")) {
            // ������������
        	i=resultNum;
            resultNum =Math.sin(resultNum/180*Math.PI);
            s="sin("+String.valueOf(i)+")="+String.valueOf(resultNum)+"\r\n";
            diplay(s);
        } else if (operator.equals("=")) {
            // ��ֵ����
            resultNum = getNumber();
        }else if (operator.equals("log")) {
            // ��������
        	if (resultNum<=0.0) {
                // �������Ϸ�
                operateValidFlag = false;
                resultText.setText("���������0����");
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
        // ����������û����İ�ť
        operator = key;
        firstDigit = true;
        operateValidFlag = true;
    }
 
    /*
     * �ӽ���ı����л�ȡ����
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
     * ����ʷ��¼�ı������������Ŀ
     */
    private void save(){
    	 if (file == null) {
    		 FileDialog save = new FileDialog(this, "����", FileDialog.SAVE);
             save.setVisible(true);//��ʾ�����ļ��Ի���
             String dirpath = save.getDirectory();//��ȡ�����ļ�·�������浽�ַ����С�
             String fileName = save.getFile();////��ȡ�򱣴��ļ����Ʋ����浽�ַ�����
             
             if (dirpath == null || fileName == null)//�ж�·�����ļ��Ƿ�Ϊ��
                 return;//�ղ���
             else
                 file=new File(dirpath,fileName);//�ļ���Ϊ�գ��½�һ��·��������
         }
             try {
                 BufferedWriter bufw = new BufferedWriter(new FileWriter(file));
                 
                 String text = histroy.getText();//��ȡ�ı�����
                 bufw.write(text);//����ȡ�ı�����д�뵽�ַ������
                 bufw.close();//�ر��ļ�
             } catch (IOException e1) {
                 //�׳�IO�쳣
                 e1.printStackTrace();
             }
    }
 
    private void diplay(String s){

        String a = null;    
        Calendar time = Calendar.getInstance();  
        a =time.get(Calendar.HOUR_OF_DAY)+"ʱ"+time.get(Calendar.MINUTE)+"��"+time.get(Calendar.SECOND)+"��  :   "; 
        histroy.append(a);
    	histroy.append(s);
    }

    private void about(){
    	
        JOptionPane.showMessageDialog(null, "���ߣ�����\r\n�༶�������1401\r\nѧ�ţ�20145293\r\nʱ�䣺2016��11��", "����",JOptionPane.INFORMATION_MESSAGE);
    	
    }

}
