package cat.uvic.teknos.shoeshop.services;

import cat.uvic.teknos.shoeshop.services.exception.ResourceNotFoundException;
import cat.uvic.teknos.shoeshop.services.exception.ServerErrorException;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;

public class RequestRouter {
    private static RawHttp rawHttp = new RawHttp();
    public RawHttpResponse<?> execRequest(RawHttpRequest request){

        var path = request.getUri().getPath();
        var method = request.getMethod();

        RawHttpResponse response = null;
        try {
            //TO DO: Router logic ho hem de fer natrus
            var json = ""; //studentController.get();
            response = rawHttp.parseResponse("HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text json\r\n" +
                    "Content-Lenght: " + json.length()+ "\r\n" +
                    "\r\n" +
                    json);


        }catch (ResourceNotFoundException exception){
            response = null;
        }catch (ServerErrorException exception){
            response = null;
        }

        return null;

    }
}
