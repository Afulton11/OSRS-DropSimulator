package graphics.events.types.exception;

import java.io.FileNotFoundException;

/**
 * Created by Andrew on 12/30/2016.
 */
public class ExceptionFileNotFoundEvent extends ExceptionEvent<FileNotFoundException> {

    public ExceptionFileNotFoundEvent(FileNotFoundException exception) {
        super(exception, Type.EXCEPTION_FILENOTFOUND);
    }
}
