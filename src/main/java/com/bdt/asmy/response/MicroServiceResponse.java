package com.bdt.asmy.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@NoArgsConstructor
public class MicroServiceResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "MM-dd-yyyy hh:mm:ss",timezone = "Asia/Kolkata")
    private Date HTTPStampServerResponseTime;
    private Integer HTTPResponseStatusCode;
    private HttpStatus httpStatus;
    private String HTTPResponsePhraseReason;
    private String HttpMessage;

    public MicroServiceResponse(Integer HTTPResponseStatusCode,
                                HttpStatus httpStatus,
                                String HTTPResponsePhraseReason,
                                String httpMessage) {
        this.HTTPStampServerResponseTime=new Date();
        this.HTTPResponseStatusCode = HTTPResponseStatusCode;
        this.httpStatus = httpStatus;
        this.HTTPResponsePhraseReason = HTTPResponsePhraseReason;
        HttpMessage = httpMessage;
    }
}
