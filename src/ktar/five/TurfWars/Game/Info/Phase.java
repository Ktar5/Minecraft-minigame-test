package ktar.five.TurfWars.Game.Info;

public enum Phase {

    startCount(0, 10, PhaseType.START_COUNT, 0),
    n1(1, 40, PhaseType.BUILDING, 64),
    n2(2, 90, PhaseType.KILLING, 1),
    n3(3, 20, PhaseType.BUILDING, 32),
    n4(4, 90, PhaseType.KILLING, 2),
    n5(5, 20, PhaseType.BUILDING, 32),
    n6(6, 90, PhaseType.KILLING, 3),
    n7(7, 20, PhaseType.BUILDING, 32),
    n8(8, 90, PhaseType.KILLING, 4),
    n9(9, 20, PhaseType.BUILDING, 32),
    n10(10, 90, PhaseType.KILLING, 5),
    n11(11, 20, PhaseType.BUILDING, 32),
    n12(12, 90, PhaseType.KILLING, 6),
    n13(13, 20, PhaseType.BUILDING, 32),
    n14(14, 90, PhaseType.KILLING, 7),
    n15(15, 20, PhaseType.BUILDING, 32),
    n16(16, 90, PhaseType.KILLING, 8),
    n17(17, 20, PhaseType.BUILDING, 32),
    n18(18, 90, PhaseType.KILLING, 9),
    n19(19, 20, PhaseType.BUILDING, 32),
    n20(20, 99999, PhaseType.KILLING, 9999);

    private final int phaseNumber;
    private final int seconds;
    private final PhaseType type;
    private final int amount;

    private Phase(int phaseNumber, int seconds, PhaseType type, int amount) {
        this.phaseNumber = phaseNumber;
        this.seconds = seconds;
        this.type = type;
        this.amount = amount;
    }

    public int getPhaseNumber() {
        return phaseNumber;
    }

    public int getSeconds() {
        return seconds;
    }

    public PhaseType getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public enum PhaseType {

        BUILDING,
        KILLING,
        START_COUNT;
        
        private PhaseType(){
            
        }

    }

}


