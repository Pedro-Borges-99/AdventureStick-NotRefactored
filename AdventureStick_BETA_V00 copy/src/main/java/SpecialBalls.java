public enum SpecialBalls {
    PLUS_ONE("RedPlus1.png"),
    IMMORTALITY("FinalStar.png");
    private String imagePath;

    SpecialBalls(String imagePath){
        this.imagePath=imagePath;
    }
    public String getImagePath(){
        return imagePath;
    }
}
