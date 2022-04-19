package code;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JToggleButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class Game implements ActionListener {

    private static Board board;
    private static JFrame window;
    private static JPanel gamePanel = new JPanel();
    private static JPanel topPanel = new JPanel();

    JToggleButton[][] buttons;
    static JPanel minespace = null;

    static ImageIcon emptyIcon = null;
    static ImageIcon tileIcon = null;
    static ImageIcon bombIcon = null;
    static ImageIcon oneIcon = null;
    static ImageIcon twoIcon = null;
    static ImageIcon threeIcon = null;
    static ImageIcon fourIcon = null;
    static ImageIcon fiveIcon = null;
    static ImageIcon sixIcon = null;
    static ImageIcon sevenIcon = null;
    static ImageIcon eightIcon = null;
    static ImageIcon flagIcon = null;

    public Game() {
        board = Board.instance(Main.numRows, Main.numCols, Main.numMines);
        window = new JFrame("Minesweeper");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setResizable(false);
        window.setJMenuBar(createMenuBar());
        loadImages();

        gamePanel.setLayout(new BorderLayout());
        gamePanel.add(topPanel, BorderLayout.NORTH);
        organizeButtons();
        window.add(gamePanel);
        window.pack();
    }

    public ImageIcon getScaledImage(String img) {
        ImageIcon icon = new ImageIcon(img);
        Image image = icon.getImage();

        Image scaledImg = image.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaledImg);
        return icon;
    }

    public void loadImages() {
        emptyIcon = getScaledImage("icons/empty.png");
        tileIcon = getScaledImage("icons/t.png");
        bombIcon = getScaledImage("icons/pit.png");
        oneIcon = getScaledImage("icons/1.png");
        twoIcon = getScaledImage("icons/2.png");
        threeIcon = getScaledImage("icons/3.png");
        fourIcon = getScaledImage("icons/4.png");
        fiveIcon = getScaledImage("icons/5.png");
        sixIcon = getScaledImage("icons/6.png");
        sevenIcon = getScaledImage("icons/7.png");
        eightIcon = getScaledImage("icons/8.png");
        flagIcon = getScaledImage("icons/flag.png");
    }

    public JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Game");
        final JMenuItem menuNew = new JMenuItem("New Game");
        menu.add(menuNew);

        ActionListener menuListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (menuNew == e.getSource()) {
                    window.dispose();
                    Main.newGame();
                }
            }
        };

        menuNew.addActionListener(menuListener);
        bar.add(menu);
        return bar;
    }    

    public void organizeButtons() {
        buttons = new JToggleButton[board.getDimensionX()][board.getDimensionY()];
        if (minespace != null) {
            gamePanel.remove(minespace);
            minespace = null;
        }
        minespace = new JPanel();
        minespace.setLayout(new GridLayout(board.getDimensionX(), board.getDimensionY()));

        for (int i = 0; i < board.getDimensionX(); ++i) {
            for (int j = 0; j < board.getDimensionY(); ++j) {
                buttons[i][j] = new JToggleButton("");
                buttons[i][j].setModel(new OneWayToggleButton());
                buttons[i][j].setBackground(Color.WHITE);
                buttons[i][j].setPreferredSize(new Dimension(42, 42));
                buttons[i][j].addActionListener(this);
                final int ii = i;
                final int jj = j;
                buttons[i][j].addMouseListener(new MouseAdapter() {
                    public void mouseReleased(MouseEvent e) {
                        if(SwingUtilities.isRightMouseButton(e)){//right click sets a flag
                            System.out.println("You right-clicked the square at: " + board.getGrid()[ii][jj].getXIndex() + ", "
                                    + board.getGrid()[ii][jj].getYIndex());
                            if(!board.getGrid()[ii][jj].getIsFlag() && board.getGrid()[ii][jj].getIsClicked()) {}
                            else if(board.getGrid()[ii][jj].getIsFlag()) {
                                board.getGrid()[ii][jj].setIsFlag(false);
                                buttons[ii][jj].setIcon(tileIcon);
                                board.getGrid()[ii][jj].setIsClicked(false);
                            } else {
                                board.getGrid()[ii][jj].setIsFlag(true);
                                buttons[ii][jj].setIcon(flagIcon);
                                board.getGrid()[ii][jj].setIsClicked(true);
                            }
                        }
                        else{//left click opens the square
                            if(!board.getGrid()[ii][jj].getIsFlag()){
                                board.getGrid()[ii][jj].setIsClicked(true);
                                System.out.println("You left-clicked the square at: " + board.getGrid()[ii][jj].getXIndex() + ", "
                                        + board.getGrid()[ii][jj].getYIndex()); 
                                System.out.println("There are " +  board.getGrid()[ii][jj].getNearbyMines() + " adjacent mines.");
                                if(board.getGrid()[ii][jj].getIsMine()) {//if the square is a mine, game over
                                    buttons[ii][jj].setIcon(bombIcon); 
                                    for (int x = 0; x < board.getDimensionX(); ++x) {
                                        for (int y = 0; y < board.getDimensionY(); ++y) {

                                            if (board.getGrid()[x][y].getIsMine()) {
                                                buttons[x][y].setIcon(bombIcon); 
                                            }
                                        }
                                    }
                                    JOptionPane.showConfirmDialog(null, "YOU LOSE!", 
                                            "Game Over", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);

                                } else {// if square is not a mine
                                    if (board.getGrid()[ii][jj].getNearbyMines() == 1) {
                                            buttons[ii][jj].setIcon(oneIcon);
                                    }
                                    if (board.getGrid()[ii][jj].getNearbyMines() == 2) {
                                        buttons[ii][jj].setIcon(twoIcon);
                                    }
                                    if (board.getGrid()[ii][jj].getNearbyMines() == 3) {
                                        buttons[ii][jj].setIcon(threeIcon);
                                    }
                                    if (board.getGrid()[ii][jj].getNearbyMines() == 4) {
                                        buttons[ii][jj].setIcon(fourIcon);
                                    }
                                    if (board.getGrid()[ii][jj].getNearbyMines() == 5) {
                                        buttons[ii][jj].setIcon(fiveIcon);
                                    }
                                    if (board.getGrid()[ii][jj].getNearbyMines() == 6) {
                                        buttons[ii][jj].setIcon(sixIcon);
                                    }
                                    if (board.getGrid()[ii][jj].getNearbyMines() == 7) {
                                        buttons[ii][jj].setIcon(sevenIcon);
                                    }
                                    if (board.getGrid()[ii][jj].getNearbyMines() == 8) {
                                        buttons[ii][jj].setIcon(eightIcon);
                                    }
                                    if (board.getGrid()[ii][jj].getNearbyMines() == 0) {
                                        buttons[ii][jj].setBackground(new Color(200, 200, 250));
                                        buttons[ii][jj].setIcon(emptyIcon);
                                    } 
                                    if(gameWon()){
                                        JOptionPane.showConfirmDialog(null, "YOU WIN!",
                                                "Game Over", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            }
                        }
                    }
                });
                buttons[i][j].setIcon(tileIcon);
                minespace.add(buttons[i][j]);
            }
        }

        minespace.setVisible(true);
        gamePanel.add(minespace, BorderLayout.CENTER);
        formatBoard();
        window.pack();
    } 

    public void formatBoard() {
        for (int i = 0; i < board.getDimensionX(); ++i) {
            for (int j = 0; j < board.getDimensionY(); ++j) {
                board.getGrid()[i][j].findNearbyMines(i, j);
            }
        }
    } 

    public static boolean lookForMineAt(int x, int y) {
        if(board.getGrid()[x][y].getIsMine() == true) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean gameWon() {
        boolean gameWon = true;
        for (int i = 0; i < board.getGrid().length; ++i) {
            for (int j = 0; j < board.getGrid()[i].length; ++j) {
                if(!board.getGrid()[i][j].getIsClicked() && !board.getGrid()[i][j].getIsMine()){
                    gameWon = false;
                }
            }
        }
        return gameWon;
    }

    @Override
    public void actionPerformed(ActionEvent e) {}
}
