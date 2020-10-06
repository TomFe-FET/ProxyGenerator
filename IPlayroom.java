public interface IPlayroom {
    public void enter(IPlayer player);

    public void leave(IPlayer player);

    public void positionStone(IPlayer player, int row);
}
