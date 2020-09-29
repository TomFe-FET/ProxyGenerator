public interface InputInterface {
    public void enter(ISpieler chatter);

    public void leave(ISpieler chatter);

    public void say(String message, ISpieler spieler);

    public void postField(char[][] spielfeld);

    public boolean positionStone(char spielfeld[][], ISpieler spieler, int row);
}
