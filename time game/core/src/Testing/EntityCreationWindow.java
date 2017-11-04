package Testing;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mygdx.time.manager.FileReader;

public class EntityCreationWindow extends JFrame{

	private JComboBox airborneField;
	private JPanel contentPane;
	private JTextField nameField;
	private JTextField spriteField;
	private JTextField categoryField;
	private JTextField maskField;
	private JTextField strengthField;
	private JTextField dexterityField;
	private JTextField intelligenceField;
	private JTextField baseSpeedField;

	/**
	 * Create the frame.
	 */
	public EntityCreationWindow() throws Exception{
		UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 371, 395);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		nameField = new JTextField();
		nameField.setFont(new Font("SansSerif", Font.PLAIN, 11));
		nameField.setBounds(102, 42, 229, 23);
		contentPane.add(nameField);
		nameField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("airborne");
		lblNewLabel.setBounds(10, 14, 82, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblName = new JLabel("name");
		lblName.setBounds(10, 42, 82, 23);
		contentPane.add(lblName);
		
		JButton btnNewButton = new JButton("Save");
		btnNewButton.setBounds(134, 322, 89, 23);
		btnNewButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	save();
		    }
		});
		contentPane.add(btnNewButton);
		
		airborneField = new JComboBox();
		airborneField.setFont(new Font("SansSerif", Font.PLAIN, 11));
		airborneField.setBounds(102, 11, 96, 20);
		airborneField.addItem("");
		airborneField.addItem("True");
		airborneField.addItem("False");
		contentPane.add(airborneField);
		
		spriteField = new JTextField();
		spriteField.setFont(new Font("SansSerif", Font.PLAIN, 11));
		spriteField.setColumns(10);
		spriteField.setBounds(102, 77, 229, 23);
		contentPane.add(spriteField);
		
		JLabel lblSprites = new JLabel("sprite");
		lblSprites.setBounds(10, 77, 82, 23);
		contentPane.add(lblSprites);
		
		categoryField = new JTextField();
		categoryField.setFont(new Font("SansSerif", Font.PLAIN, 11));
		categoryField.setColumns(10);
		categoryField.setBounds(102, 112, 229, 23);
		contentPane.add(categoryField);
		
		JLabel lblCategory = new JLabel("category");
		lblCategory.setBounds(10, 112, 82, 23);
		contentPane.add(lblCategory);
		
		maskField = new JTextField();
		maskField.setFont(new Font("SansSerif", Font.PLAIN, 11));
		maskField.setColumns(10);
		maskField.setBounds(102, 147, 229, 23);
		contentPane.add(maskField);
		
		JLabel lblMask = new JLabel("mask");
		lblMask.setBounds(10, 147, 82, 23);
		contentPane.add(lblMask);
		
		strengthField = new JTextField();
		strengthField.setFont(new Font("SansSerif", Font.PLAIN, 11));
		strengthField.setColumns(10);
		strengthField.setBounds(102, 182, 229, 23);
		contentPane.add(strengthField);
		
		JLabel lblStrength = new JLabel("strength");
		lblStrength.setBounds(10, 182, 82, 23);
		contentPane.add(lblStrength);
		
		dexterityField = new JTextField();
		dexterityField.setFont(new Font("SansSerif", Font.PLAIN, 11));
		dexterityField.setColumns(10);
		dexterityField.setBounds(102, 217, 229, 23);
		contentPane.add(dexterityField);
		
		JLabel lblDexterity = new JLabel("dexterity");
		lblDexterity.setBounds(10, 217, 82, 23);
		contentPane.add(lblDexterity);
		
		intelligenceField = new JTextField();
		intelligenceField.setFont(new Font("SansSerif", Font.PLAIN, 11));
		intelligenceField.setColumns(10);
		intelligenceField.setBounds(102, 252, 229, 23);
		contentPane.add(intelligenceField);
		
		JLabel lblInt = new JLabel("intelligence");
		lblInt.setBounds(10, 252, 82, 23);
		contentPane.add(lblInt);
		
		baseSpeedField = new JTextField();
		baseSpeedField.setFont(new Font("SansSerif", Font.PLAIN, 11));
		baseSpeedField.setColumns(10);
		baseSpeedField.setBounds(102, 287, 229, 23);
		contentPane.add(baseSpeedField);
		
		JLabel lblBaseSpeed = new JLabel("base speed");
		lblBaseSpeed.setBounds(10, 287, 82, 23);
		contentPane.add(lblBaseSpeed);
	}
	
	public void save(){
		String path = "assets/entities/";
		path += nameField.getText() + ".json";
		
		JSONArray category = new JSONArray().put(categoryField.getText().split(", "));
		JSONArray mask = new JSONArray().put(maskField.getText().split(", "));
		
		
		JSONObject obj = new JSONObject();
		obj.put("name", nameField.getText());
		obj.put("sprite", spriteField.getText());
		obj.put("category", category);
		obj.put("mask", mask);
		obj.put("airborne", airborneField.getSelectedItem());
		obj.put("strength", strengthField.getText());
		obj.put("dexterity", dexterityField.getText());
		obj.put("intelligence", intelligenceField.getText());
		obj.put("base speed", baseSpeedField.getText());
		
		try {
			FileReader.writeFile(path, obj.toString(4));
			System.out.println(FileReader.readFile(path));
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
