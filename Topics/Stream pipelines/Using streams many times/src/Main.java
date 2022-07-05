import java.util.List;
import java.util.function.*;
import java.util.stream.*;

class FunctionUtils {

    public static <T> Supplier<Stream<T>> saveStream(Stream<T> stream){
        // write your code here
        List<T> targetLongList = stream
                .collect(Collectors.toList());

        Supplier<Stream<T>> streamSupplier = () -> targetLongList.stream();

        return streamSupplier;
    }

}