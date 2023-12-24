import java.util.List;

public class DecoderResult {
    List<Object> result;
    int nextIndex;

    public DecoderResult(List<Object> result, int index) {
        this.result = result;
        this.nextIndex = index;
    }

    public void setResult(List<Object> result) {
        this.result = result;
    }

    public void setNextIndex(int nextIndex) {
        this.nextIndex = nextIndex;
    }
}
