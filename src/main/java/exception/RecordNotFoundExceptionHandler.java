package exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RecordNotFoundExceptionHandler implements ExceptionMapper<RecordNotFoundException> {
    @Override
    public Response toResponse(RecordNotFoundException exception) {
        return Response.status(404).build();
    }
}
