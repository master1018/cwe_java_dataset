
package org.dpppt.backend.sdk.ws.radarcovid.exception;
import org.springframework.http.HttpStatus;
public class RadarCovidServerException extends RuntimeException {
    private final HttpStatus httpStatus;
    public RadarCovidServerException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
}
