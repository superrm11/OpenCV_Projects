package visionUtility;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class CameraSelectWindow extends JFrame
{

	private JPanel contentPane;

	private JTextField ipSel;

	private JRadioButton rdbtnIpCamera;

	private JRadioButton rdbtnUsbCamera;

	private JSpinner usbCamSel;

	private String ip;

	private int usbCam;

	private boolean isUsingUsbCam = true;

	/**
	 * Create the frame.
	 */
	public CameraSelectWindow()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 275, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		ipSel = new JTextField();
		ipSel.setBounds(77, 74, 116, 22);
		contentPane.add(ipSel);
		ipSel.setColumns(10);

		rdbtnIpCamera = new JRadioButton("IP Camera");
		rdbtnIpCamera.setBounds(0, 45, 127, 25);
		contentPane.add(rdbtnIpCamera);
		rdbtnIpCamera.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				rdbtnUsbCamera.setSelected(false);
				usbCamSel.setEnabled(false);
				ipSel.setEnabled(true);
				ipSel.setText(ip);
			}
		});

		rdbtnUsbCamera = new JRadioButton("USB Camera");
		rdbtnUsbCamera.setBounds(0, 0, 101, 25);
		contentPane.add(rdbtnUsbCamera);
		rdbtnUsbCamera.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				rdbtnIpCamera.setSelected(false);
				ipSel.setEnabled(false);
				usbCamSel.setEnabled(true);
				usbCamSel.setValue(usbCam);
			}
		});

		usbCamSel = new JSpinner();
		usbCamSel.setBounds(150, 13, 43, 22);
		contentPane.add(usbCamSel);

		JLabel lblCam = new JLabel("Cam #");
		lblCam.setBounds(105, 16, 43, 20);
		contentPane.add(lblCam);

		JLabel lblIp = new JLabel(".mjpg link");
		lblIp.setBounds(10, 77, 65, 16);
		contentPane.add(lblIp);

		JButton btnSave = new JButton("Save");
		btnSave.setBounds(150, 115, 97, 25);
		contentPane.add(btnSave);

		btnSave.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				saveItems();
				dispose();
			}
		});

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(30, 115, 97, 25);
		contentPane.add(btnCancel);

		btnCancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}

		});
		this.setVisible(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public String getIP()
	{
		return this.ip;
	}

	public int getCamNum()
	{
		return this.usbCam;
	}

	public boolean isUsingUsbCam()
	{
		return this.isUsingUsbCam;
	}

	private void saveItems()
	{
		this.ip = this.ipSel.getText();
		this.usbCam = (int) this.usbCamSel.getValue();
		this.isUsingUsbCam = this.rdbtnUsbCamera.isSelected();
	}

	private void openItems()
	{
		this.ipSel.setText(this.ip);
		this.usbCamSel.setValue(this.usbCam);
		this.rdbtnUsbCamera.setSelected(this.isUsingUsbCam);
		this.rdbtnIpCamera.setSelected(!this.isUsingUsbCam);
	}

	public void displayWindow()
	{
		this.setVisible(true);
		this.openItems();
	}

}
