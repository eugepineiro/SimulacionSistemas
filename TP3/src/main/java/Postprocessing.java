import java.util.List;

public class Postprocessing {

    /* JSON
    [
        {
            "times": [
                [1,2,3,4], // simu 1 no se si hacerlo por simu tho...
                [1,2,3,4], // simu 2
                [1,2,3,4], // simu 3
            ],
            "N": 100
         },
         {
            "times": [
                [1,2,3,4], // simu 1
                [1,2,3,4], // simu 2
                [1,2,3,4], // simu 3
            ],
            "N": 125
         },
         {
            "times": [
                [1,2,3,4], // simu 1
                [1,2,3,4], // simu 2
                [1,2,3,4], // simu 3
            ],
            "N": 150
         },

         //idem para speeds


    ]

     */

    private List<List<Double>> times;
    private List<List<Double>> speeds;

    public List<List<Double>> getTimes() {
        return times;
    }

    public void setTimes(List<List<Double>> times) {
        this.times = times;
    }

    public List<List<Double>> getSpeeds() {
        return speeds;
    }

    public void setSpeeds(List<List<Double>> speeds) {
        this.speeds = speeds;
    }
}
