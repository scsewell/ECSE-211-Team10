package main;

public class Main
{
    private StartParameters m_startParams;
    private Board m_board;
    private Odometer m_odometer;
    
    /**
     * Launches the main program.
     */
    public static void main(String[] args)
    {
        Main main = new Main();
        main.launch();
    }
    
    /**
     * Gets starting info, runs threads, and begins the main logic loop.
     */
    private void launch()
    {
        // wait to progress until start information is received via wifi
        m_startParams = new StartParameters();
        while (!m_startParams.hasRecievedData())
        {
            m_startParams.getWifiData();
            //m_startParams.useTestData(); // use for testing to avoid having to launch wifi server
        }
        
        // get the board
        m_board = m_startParams.getBoard();
        
        Odometer odometer = new Odometer();
        odometer.start();
    }
}