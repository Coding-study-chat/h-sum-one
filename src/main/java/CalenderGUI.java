package main.java;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class CalenderGUI extends JFrame {

    String[] dayAr = {"Sun", "Mon", "Tue", "Wen", "Thur", "Fri", "Sat"};
    DateBox[] dateBoxAr = new DateBox[dayAr.length * 6];
    JPanel p_north;
    JButton bt_prev;
    JLabel lb_title;
    JButton bt_next;
    JPanel p_center;
    Calendar cal;

    int yy;
    int mm;
    int startDay;
    int lastDate; 

    
    public CalenderGUI() {

        p_north = new JPanel();
        bt_prev = new JButton("before");
        lb_title = new JLabel("", SwingConstants.CENTER);
        bt_next = new JButton("next");
        p_center = new JPanel();

        lb_title.setFont(new Font("Arial-Black", Font.BOLD, 25));
        lb_title.setPreferredSize(new Dimension(300, 30));

        p_north.add(bt_prev);
        p_north.add(lb_title);
        p_north.add(bt_next);
        add(p_north, BorderLayout.NORTH);
        add(p_center);

        bt_prev.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateMonth(-1);
            }
        });

        bt_next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateMonth(1);
            }
        });
        JPanel p_south = new JPanel();
        add(p_south, BorderLayout.SOUTH);

        JButton backBtn = new JButton("back to home");
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        p_south.add(backBtn);
        getCurrentDate();
        getDateInfo();
        setDateTitle();
        createDay();
        createDate();
        printDate();

        setVisible(true);
        setBounds(100, 100, 780, 780);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                setVisible(false);
            }
        });
    }

    public void getCurrentDate() {
        cal = Calendar.getInstance();
    }

    public void getDateInfo() {
        yy = cal.get(Calendar.YEAR);
        mm = cal.get(Calendar.MONTH);
        startDay = getFirstDayOfMonth(yy, mm);
        lastDate = getLastDate(yy, mm);
    }

    public void createDay() {
        for (int i = 0; i < 7; i++) {
            DateBox dayBox = new DateBox(dayAr[i], Color.gray, 100, 70);
            p_center.add(dayBox);
        }
    }

    public void createDate() {
        for (int i = 0; i < dayAr.length * 6; i++) {
            DateBox dateBox = new DateBox("", Color.LIGHT_GRAY, 100, 100);
            p_center.add(dateBox);
            dateBoxAr[i] = dateBox;
        }
    }

    public int getFirstDayOfMonth(int yy, int mm) {
        Calendar cal = Calendar.getInstance();
        cal.set(yy, mm, 1);
        return cal.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public int getLastDate(int yy, int mm) {
        Calendar cal = Calendar.getInstance();
        cal.set(yy, mm + 1, 0);
        return cal.get(Calendar.DATE);
    }

    public void printDate() {
        int n = 1;
        for (int i = 0; i < dateBoxAr.length; i++) {
            if (i >= startDay && n <= lastDate) {
                dateBoxAr[i].day = Integer.toString(n);
                dateBoxAr[i].repaint();
                n++;
            } else {
                dateBoxAr[i].day = "";
                dateBoxAr[i].repaint();
            }
        }
    }

    public void updateMonth(int data) {
        cal.set(Calendar.MONTH, mm + data);
        getDateInfo();
        printDate();
        setDateTitle();
    }

    public void setDateTitle() {
        lb_title.setText(yy + "-" + (mm + 1));
        lb_title.updateUI();
    }

    public static void main(String[] args) {
        new CalenderGUI();
    }

    public class DateBox extends JPanel {

        String day;
        Color color;
        int width;
        int height;

        public DateBox(String day, Color color, int width, int height) {
            this.day = day;
            this.color = color;
            this.width = width;
            this.height = height;
            setPreferredSize(new Dimension(width, height));
        }

        public void paint(Graphics g) {
            String[] YMD = MainGUI.firstDay.split("/");
            int _year = Integer.parseInt(YMD[0]);
            int _month = Integer.parseInt(YMD[1]);
            int _day = Integer.parseInt(YMD[2]);

            if (day.equals(Integer.toString(_day)) && yy == _year && mm == _month - 1) {
                g.setColor(Color.RED);
                g.fillRect(0, 0, width, height);

                g.setColor(Color.YELLOW);
                g.setFont(new Font("Arial", Font.BOLD, 12));
                g.drawString(day, 10, 20);
                g.drawString("first dat!", 10, 40);
            } else {
                g.setColor(color);
                g.fillRect(0, 0, width, height);

                g.setColor(Color.yellow);
                g.drawString(day, 10, 20);
            }
            for (int i = 1; i <= 10; i++) {
                if (day.equals(Integer.toString(_day)) && yy == _year + i && mm == _month - 1) {
                    g.setColor(Color.PINK);
                    g.fillRect(0, 0, width, height);

                    g.setColor(Color.YELLOW);
                    g.setFont(new Font("Arial", Font.BOLD, 12));
                    g.drawString(day, 10, 20);
                    g.drawString(i + "year!", 10, 40);
                }
            }
            Calendar FirstDay = Calendar.getInstance();
            Calendar Hdrday = Calendar.getInstance();

            FirstDay.set(_year, _month - 1, _day);
            for (int j = 100; j <= 1000; j += 100) {
                Hdrday.setTimeInMillis(FirstDay.getTimeInMillis());
                Hdrday.add(Calendar.DAY_OF_MONTH, j);

                int hdrYear = Hdrday.get(Calendar.YEAR);
                int hdrMonth = Hdrday.get(Calendar.MONTH) + 1;
                int hdrDay = Hdrday.get(Calendar.DAY_OF_MONTH);

                if (day.equals(Integer.toString(hdrDay)) && yy == hdrYear && mm == hdrMonth - 1) {
                    g.setColor(Color.PINK);
                    g.fillRect(0, 0, width, height);

                    g.setColor(Color.YELLOW);
                    g.setFont(new Font("Arial", Font.BOLD, 12));
                    g.drawString(day, 10, 20);
                    g.drawString(j + "day!", 10, 40);
                }
            }
        }
    }
}